package app.minkey.fr.minkeybackend.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BrevoConfig {

    @Value("${brevo.key}")
    private String key;
    @Value("${brevo.url}")
    private String url;
    @Value("${brevo.enabled}")
    private Boolean enabled;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl(url)
                .defaultHeader("api-key", key)
                .build();
    }
}
