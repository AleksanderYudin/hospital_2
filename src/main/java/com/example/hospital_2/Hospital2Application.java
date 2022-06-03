package com.example.hospital_2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@SpringBootApplication
public class Hospital2Application implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(Hospital2Application.class, args);
	}

//	private final ApplicationContext applicationContext;
//
//	@Autowired
//	public Hospital2Application(ApplicationContext applicationContext) {
//		this.applicationContext = applicationContext;
//	}
//
//	@Bean
//	public SpringResourceTemplateResolver templateResolver() {
//		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//		templateResolver.setApplicationContext(applicationContext);
//		templateResolver.setPrefix("");
//		templateResolver.setSuffix(".html");
//		return templateResolver;
//	}
//
//	@Bean
//	public SpringTemplateEngine templateEngine() {
//		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//		templateEngine.setTemplateResolver(templateResolver());
//		templateEngine.setEnableSpringELCompiler(true);
//		return templateEngine;
//	}
//
//	@Override
//	public void configureViewResolvers(ViewResolverRegistry registry) {
//		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//		resolver.setTemplateEngine(templateEngine());
//		registry.viewResolver(resolver);
//	}
	@Bean
	public PasswordEncoder getPasswordEncoder(){
		return new BCryptPasswordEncoder(8);
	}
}
