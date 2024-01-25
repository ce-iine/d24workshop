package paf.day24.workshop.d24workshop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import paf.day24.workshop.d24workshop.service.MessagePoller;
import paf.day24.workshop.d24workshop.service.MessageSvc;

@EnableAsync
@SpringBootApplication
public class D24workshopApplication implements CommandLineRunner{
	//mvn spring-boot:run -Dspring-boot.run.arguments=newuser
	//keys *
	//LRANGE registrations 0 -1

	@Autowired
	MessageSvc messageSvc;

	@Autowired 
	MessagePoller messagePoller;

	@Autowired
	private ApplicationArguments applicationArguments;

	public static void main(String[] args) {
		SpringApplication.run(D24workshopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		List<String> customerNameArgs= applicationArguments.getNonOptionArgs();
		String name = customerNameArgs.get(0);

		messageSvc.addToRegistration(name);
		
		messagePoller.start(name);

	}

}
