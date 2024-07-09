package uk.tojourn.sainsburystechtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//I deliberately do not use lombok as I think it creates easier to read code instead of having loads of annotations everywhere

@SpringBootApplication
public class SainsburysTechTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(SainsburysTechTestApplication.class, args);
	}

}
