package com.booksearch;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class BookController {

//    private final BookRepository bookRepository;
//
//    BookController(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
    @Bean
    RouterFunction<ServerResponse> routes(BookRepository br) {
        return RouterFunctions.route(GET("/books"), serverRequest -> ok().body(br.findAll(), Book.class));
    }

    @RequestMapping(value = "/")
       public String index() {
          return "index";
       }
//    @GetMapping("/")
//    public String home(Model model) {
////        Flux<Book> books = bookRepository.findAll();
//        model.addAttribute("books", "books");
//        return "/resources/templates/index";
//    }

}