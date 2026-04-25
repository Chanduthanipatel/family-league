package fourth.project.end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EndProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EndProjectApplication.class, args);
	}

}
