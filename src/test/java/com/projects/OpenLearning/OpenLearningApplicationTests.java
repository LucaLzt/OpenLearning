package com.projects.OpenLearning;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenLearningApplicationTests extends AbstractIntegrationTest {

	@Test
	void contextLoads() {
		// 1. Levanta el contexto de Spring
		// 2. Levanta Postgres en Docker
		// 3. Levanta Redis en Docker
		// 4. Levanta MinIO en Docker
		// 5. La aplicación se conectó a los tres servicios
	}

}
