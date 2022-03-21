package no.ntnu.appdev.group15.teawebsitebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@SpringBootApplication
public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Docket swaggerConfiguration(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/products/*"))
				.apis(RequestHandlerSelectors.basePackage("no.ntnu.appdev.group15"))
				.build()
				.apiInfo(apiDetails());
	}

	private ApiInfo apiDetails(){
		return new ApiInfo(
				"TeaWebsite backend",
				"A backend for a tea website. This is a school project.",
				"1.0",
				"Luli - pepeclap",
				new Contact("Luli", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "luli@lul.com"),
				"API License",
				"lul",
				Collections.emptyList()
		);
	}
}
