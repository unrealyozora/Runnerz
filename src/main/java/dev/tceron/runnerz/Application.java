package dev.tceron.runnerz;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.tceron.runnerz.run.Run;
import dev.tceron.runnerz.run.Location;



@SpringBootApplication
public class Application {

	private static final Logger log = Logger.getLogger(Application.class.getName());
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner runner(){
		return args -> {
			Run run = new Run (1, "Morning Run", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 5, Location.OUTDOOR);
			log.info("Run: "+run);
		};
	}
}
 