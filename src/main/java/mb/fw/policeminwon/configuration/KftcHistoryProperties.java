package mb.fw.policeminwon.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "kftc.history")
@ConditionalOnProperty(prefix = "kftc.history", name = "enabled", havingValue = "true")
public class KftcHistoryProperties {
	private String insertSql;
	private String selectSql;
}
