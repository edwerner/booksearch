package com.booksearch;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class BookController {

    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    @Bean
    RouterFunction<ServerResponse> routes(BookRepository br) {
        return RouterFunctions.route(GET("/bookfeed"), serverRequest -> ok().body(br.findAll(), Book.class));
    }

    @RequestMapping(value = "/books")
      public String index(final Model model) {
    
        IReactiveDataDriverContextVariable books =
                new ReactiveDataDriverContextVariable(bookRepository.findAll());

        model.addAttribute("books", books);
    	
    	return "index";
      }
}