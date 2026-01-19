package github.com.AlexxCobb.Task_tracker.telegram.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TaskTrackerTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerTelegramBotApplication.class, args);
	}

}
