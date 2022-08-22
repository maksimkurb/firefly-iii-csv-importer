package ru.cubly.firefly.importer.oauth

import com.nimbusds.oauth2.sdk.ErrorObject
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse
import net.minidev.json.JSONObject
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService
import org.springframework.security.oauth2.core.AuthenticationMethod
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.util.Assert
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.UnsupportedMediaTypeException
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.cubly.firefly.importer.entity.FireflyUser
import ru.cubly.firefly.model.UserSingle

class FireflyOAuth2UserService : ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
    companion object {
        private const val INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response"
        private const val MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri"

        private inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
        private val USER_OBJECT = typeReference<UserSingle>()
        private val STRING_STRING_MAP = typeReference<Map<String, String>>()
    }

    private var webClient = WebClient.create()

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): Mono<OAuth2User> {
        return Mono.defer {
            Assert.notNull(userRequest, "userRequest cannot be null")
            val userInfoUri = userRequest.clientRegistration.providerDetails.userInfoEndpoint
                .uri
            if (!StringUtils.hasText(userInfoUri)) {
                val oauth2Error =
                    OAuth2Error(
                        MISSING_USER_INFO_URI_ERROR_CODE,
                        "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "
                                + userRequest.clientRegistration.registrationId,
                        null
                    )
                throw OAuth2AuthenticationException(oauth2Error, oauth2Error.toString())
            }
            val authenticationMethod = userRequest.clientRegistration.providerDetails
                .userInfoEndpoint.authenticationMethod
            val requestHeadersSpec = getRequestHeaderSpec(
                userRequest, userInfoUri,
                authenticationMethod
            )
            // @formatter:off
            val userAttributes =
                requestHeadersSpec.retrieve()
                    .onStatus(
                        { obj: HttpStatus -> obj.isError },
                        { response: ClientResponse ->
                            parse(response)
                                .map<Throwable> { userInfoErrorResponse: UserInfoErrorResponse ->
                                    val description: String = userInfoErrorResponse.errorObject.description
                                    val oauth2Error = OAuth2Error(
                                        INVALID_USER_INFO_RESPONSE_ERROR_CODE, description,
                                        null
                                    )
                                    throw OAuth2AuthenticationException(oauth2Error, oauth2Error.toString())
                                }
                        }
                    )
                    .bodyToMono(USER_OBJECT)
            userAttributes.map { user: UserSingle ->
                val attrs = buildUserAttributes(user)

                val authority: GrantedAuthority = OAuth2UserAuthority(attrs)
                val authorities: MutableSet<GrantedAuthority> = HashSet()
                authorities.add(authority)
                val token: OAuth2AccessToken = userRequest.accessToken
                for (scope: String in token.scopes) {
                    authorities.add(SimpleGrantedAuthority("SCOPE_$scope"))
                }
                FireflyUser(authorities,
                    user.data.id.toLong(),
                    user.data.attributes.email,
                    attrs)
            }
                .onErrorMap({ ex: Throwable ->
                    (ex is UnsupportedMediaTypeException ||
                            ex.cause is UnsupportedMediaTypeException)
                }, { ex: Throwable ->
                    val contentType: String =
                        if ((ex is UnsupportedMediaTypeException)) ex.contentType
                            .toString() else (ex.cause as UnsupportedMediaTypeException?)!!.contentType.toString()
                    val errorMessage: String =
                        ("An error occurred while attempting to retrieve the UserInfo Resource from '"
                                + userRequest.clientRegistration.providerDetails.userInfoEndpoint
                            .uri
                                + "': response contains invalid content type '" + contentType + "'. "
                                + "The UserInfo Response should return a JSON object (content type 'application/json') "
                                + "that contains a collection of name and value pairs of the claims about the authenticated End-User. "
                                + "Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration '"
                                + userRequest.clientRegistration.registrationId
                                + "' conforms to the UserInfo Endpoint, "
                                + "as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'")
                    val oauth2Error = OAuth2Error(
                        INVALID_USER_INFO_RESPONSE_ERROR_CODE, errorMessage,
                        null
                    )
                    throw OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex)
                })
                .onErrorMap { ex: Throwable ->
                    val oauth2Error = OAuth2Error(
                        INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                        "An error occurred reading the UserInfo response: " + ex.message, null
                    )
                    OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex)
                }
        }
        // @formatter:on
    }

    private fun buildUserAttributes(user: UserSingle): Map<String, Any> {
        return mapOf(
            "userId" to user.data.id,
            "email" to user.data.attributes.email,
            "blocked" to (user.data.attributes.blocked ?: false)
        )
    }

    private fun getRequestHeaderSpec(
        userRequest: OAuth2UserRequest, userInfoUri: String,
        authenticationMethod: AuthenticationMethod
    ): WebClient.RequestHeadersSpec<*> {
        return if (AuthenticationMethod.FORM == authenticationMethod) {
            // @formatter:off
            webClient.post()
                .uri(userInfoUri)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue("access_token=" + userRequest.accessToken.tokenValue)
            // @formatter:on
        } else webClient.get()
            .uri(userInfoUri)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .headers { headers: HttpHeaders ->
                headers
                    .setBearerAuth(userRequest.accessToken.tokenValue)
            }
        // @formatter:off
        // @formatter:on
    }

    /**
     * Sets the [WebClient] used for retrieving the user endpoint
     * @param webClient the client to use
     */
    fun setWebClient(webClient: WebClient) {
        Assert.notNull(webClient, "webClient cannot be null")
        this.webClient = webClient
    }

    private fun parse(httpResponse: ClientResponse): Mono<UserInfoErrorResponse> {
        val wwwAuth = httpResponse.headers().asHttpHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE)
        return if (StringUtils.hasLength(wwwAuth)) {
            // Bearer token error?
            Mono.fromCallable {
                UserInfoErrorResponse.parse(
                    wwwAuth
                )
            }
        } else httpResponse.bodyToMono(STRING_STRING_MAP)
            .map { body: Map<String, String> ->
                UserInfoErrorResponse(
                    ErrorObject.parse(JSONObject(body))
                )
            }
        // Other error?
    }

}