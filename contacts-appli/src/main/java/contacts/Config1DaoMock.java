package contacts;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;


@Lazy
@ComponentScan(
		basePackages = {
				"contacts.javafx.data.mapper",
				"contacts.javafx.view",
				"contacts.javafx.model.standard",
				"contacts.emb.data.mapper",
				"contacts.emb.service.util",
				"contacts.emb.service.standard",
				"contacts.emb.dao.mock",
		},
		lazyInit = true	)
public class Config1DaoMock {
	
}
