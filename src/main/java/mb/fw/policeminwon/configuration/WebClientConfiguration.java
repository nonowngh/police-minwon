package mb.fw.policeminwon.configuration;

import org.apache.http.HttpHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "web.client", ignoreUnknownFields = true)
@ConditionalOnProperty(name = "web.client.enabled", havingValue = "true")
public class WebClientConfiguration {
	
	private String targetUrl;
	
	private String callBackUrl;
	
    @Bean(name="webClient")
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl(targetUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    @Bean(name="callBackWebClient")
    WebClient callBackWebClient() {
        return WebClient.builder()
                .baseUrl(callBackUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
