package mb.fw.policeminwon.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcConfiguration {

	@Bean
	JdbcTemplate jdbcTemplate(@Autowired(required = false) DataSource dataSource) {
	    return dataSource != null ? new JdbcTemplate(dataSource) : null;
	}
}
