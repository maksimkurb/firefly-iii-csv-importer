package ru.cubly.firefly.importer.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.core.user.OAuth2User
import java.io.Serializable
import java.util.*

class FireflyUser(
    authorities: Collection<GrantedAuthority>?,
    userId: Long,
    username: String,
    attributes: Map<String, Any>
) :
    OAuth2User, Serializable {

    private val authorities: Set<GrantedAuthority>

    private val attributes: Map<String, Any>

    private val userId: Long

    private val username: String

    init {
        this.authorities = if (authorities != null) Collections.unmodifiableSet(
            LinkedHashSet(sortAuthorities(authorities))
        ) else Collections.unmodifiableSet(LinkedHashSet(AuthorityUtils.NO_AUTHORITIES))
        this.userId = userId
        this.username = username
        this.attributes = Collections.unmodifiableMap(LinkedHashMap(attributes))
    }

    fun getUserId(): Long {
        return userId
    }

    override fun getName(): String {
        return username
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.toMutableList()
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return attributes.toMutableMap()
    }

    private fun sortAuthorities(authorities: Collection<GrantedAuthority>): MutableSet<GrantedAuthority> {
        val sortedAuthorities: SortedSet<GrantedAuthority> = TreeSet(
            Comparator.comparing { obj: GrantedAuthority -> obj.authority })
        sortedAuthorities.addAll(authorities)
        return sortedAuthorities
    }
}