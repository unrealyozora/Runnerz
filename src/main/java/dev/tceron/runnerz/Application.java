package dev.tceron.runnerz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties.Restclient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import dev.tceron.runnerz.run.Run;
import dev.tceron.runnerz.user.User;
import dev.tceron.runnerz.user.UserHttpClient;
import dev.tceron.runnerz.user.UserRestClient;
import dev.tceron.runnerz.run.JdbcClientRunRepository;
import dev.tceron.runnerz.run.Location;



@SpringBootApplication
public class Application {

	private static final Logger log = Logger.getLogger(Application.class.getName());
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Bean
	UserHttpClient userHttpClient() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
		return factory.createClient(UserHttpClient.class);
	}

	@Bean
	CommandLineRunner runner(UserHttpClient client) {
		return args -> {
			List<User> users = client.findAll();
			System.out.println("Users: " + users);
			User user1 = client.findById(1);
			System.out.println("User 1: " + user1);
		};
	}
}
 