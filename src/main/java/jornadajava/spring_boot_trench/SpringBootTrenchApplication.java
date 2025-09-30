package jornadajava.spring_boot_trench;

import jornadajava.spring_boot_trench.config.ConnectionConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConnectionConfigurationProperties.class)
public class SpringBootTrenchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTrenchApplication.class, args);
	}

}
