package com.project.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		/*
		 * new SpringApplicationBuilder(TmsApplication.class).bannerMode(
		 * Banner.Mode.CONSOLE).run(args);
		 * System.setProperty("spring.devtools.restart.enabled", "false");
		 * System.setProperty("spring.devtools.livereload.enabled", "true");
		 */
		SpringApplication.run(Application.class, args);

	}

}
