package contacts;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import jfox.jdbc.DataSourceSingleConnection;


@Lazy
@ComponentScan(
		basePackages = {
				"contacts.javafx.data.mapper",
				"contacts.javafx.view",
				"contacts.javafx.model.standard",
				"contacts.emb.data.mapper",
				"contacts.emb.service.util",
				"contacts.emb.service.standard",
				"contacts.emb.dao.jdbc",
		},
		lazyInit = true	)
public class Config2DaoJdbc {
	@Bean
	public DataSource dataSource() {
		return new DataSourceSingleConnection("classpath:META-INF/jdbc.properties" );
	}
}
