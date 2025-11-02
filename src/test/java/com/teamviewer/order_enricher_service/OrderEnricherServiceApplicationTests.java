package com.teamviewer.order_enricher_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
		// If you have Flyway/Liquibase, uncomment one of these:
		// "spring.flyway.enabled=false",
		// "spring.liquibase.enabled=false"
})
class OrderEnricherServiceApplicationTests {

	@Test
	void contextLoads() { }
}
