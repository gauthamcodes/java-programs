package com.chat.room;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.chat.room.config.JwtFilter;

@SpringBootApplication
public class Application {
	
	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		List<String> urlPatterns = new ArrayList<>();
		urlPatterns.add("/user/all");
		registrationBean.setFilter(new JwtFilter());
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
