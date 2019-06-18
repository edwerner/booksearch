package com.booksearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class BooksearchApplication {

	@Bean
	RouterFunction<ServerResponse> routes(BookRepository br) {
		return RouterFunctions.route(GET("/books"), serverRequest -> ok().body(br.findAll(), Book.class));
	}

	public static void main(String[] args) {
		SpringApplication.run(BooksearchApplication.class, args);
	}
}