package net.sytes.studyshuffle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(
    ignoreResourceNotFound = false,
    value = {
        "file:/etc/studyshuffle/application.properties"
    }
)
public class StudyshuffleApplication {
   
	public static void main(String[] args) {     
		SpringApplication.run(StudyshuffleApplication.class, args);
	}

}
