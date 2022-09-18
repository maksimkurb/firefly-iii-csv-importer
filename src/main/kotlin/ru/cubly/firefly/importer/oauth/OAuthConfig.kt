package ru.cubly.firefly.importer.oauth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Flux
import java.nio.charset.StandardCharsets


@Configuration
@EnableWebFluxSecurity
class OAuthConfig {

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
//            oauth2Client {
//                clientRegistrationRepository = clientRegistrationRepository()
//                authorizedClientRepository = authorizedClientRepository()
//                authorizationRequestRepository = authorizedRequestRepository()
//                authenticationConverter = authenticationConverter()
//                authenticationManager = authenticationManager()
//
//            }

            httpBasic {
                disable()
            }

            formLogin {
                disable()
            }

            csrf {
                disable()
            }

            cors {
                val configurationSource = UrlBasedCorsConfigurationSource()
                val config = CorsConfiguration()

                config.addAllowedOrigin("http://localhost:8123")
                config.addAllowedOrigin("http://localhost:3000")
                config.addAllowedHeader("*")
                config.allowCredentials = true
                config.addAllowedMethod("GET")
                config.addAllowedMethod("PUT")
                config.addAllowedMethod("POST")
                configurationSource.registerCorsConfiguration("/**", config)

                this.configurationSource = configurationSource
            }

            exceptionHandling {
                authenticationEntryPoint = ServerAuthenticationEntryPoint { exchange, _ ->
                    val response: ServerHttpResponse = exchange.response
                    response.statusCode = HttpStatus.UNAUTHORIZED
                    val bytes: ByteArray = "Unauthorized".toByteArray(StandardCharsets.UTF_8)
                    val buffer: DataBuffer = response.bufferFactory().wrap(bytes)
                    return@ServerAuthenticationEntryPoint response.writeWith(Flux.just(buffer))
                }
            }

            oauth2Login {
            }

            authorizeExchange {
                authorize("/api/info/**", permitAll)
                authorize("/api/**", authenticated)
                authorize(anyExchange, permitAll)
            }
        }
    }

    @Bean
    fun oauth2UserService(): ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return FireflyOAuth2UserService()
    }

    @Bean
    fun authorizedClientManager(
        clientRegistrationRepository: ReactiveClientRegistrationRepository,
        authorizedClientRepository: ServerOAuth2AuthorizedClientRepository
    ): ReactiveOAuth2AuthorizedClientManager {
        val authorizedClientProvider: ReactiveOAuth2AuthorizedClientProvider =
            ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
//            .refreshToken()
                .build()
        val authorizedClientManager = DefaultReactiveOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientRepository
        )
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }
}