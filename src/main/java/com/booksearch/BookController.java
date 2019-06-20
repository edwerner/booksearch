package com.booksearch;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

//    private final BookRepository bookRepository;
//
//    BookController(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }

    @GetMapping("/")
    public String greeting(Model model) {
//        Flux<Book> books = bookRepository.findAll();
//        model.addAttribute("books", books);
        return "index";
    }

}