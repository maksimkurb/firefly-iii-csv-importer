package ru.cubly.firefly.importer.config;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "firefly-iii")
@Validated
@Data
public class FireflyProperties {
    @URL
    @NotEmpty
    private String baseUrl;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;

    private String scope = "";

    private String getAuthUrl() {
        return baseUrl + "/oauth/authorize";
    }

    private String getAccessTokenUrl() {
        return baseUrl + "/oauth/token";
    }
}
