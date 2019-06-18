package com.booksearch;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DataWriter implements ApplicationRunner {
    private final BookRepository bookRepository;

    DataWriter(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Book book = new Book();
        book.setTitle("On the Road");
        book.setAuthor("Jack Kerouac");
        book.setIsbn("12345678");
        book.setLanguage("us-en");
        book.setRating("5");
        book.setYear("1957");
        book.setLgImage("https://img.thriftbooks.com/api/images/l/50f77ef9553d221575b1e3006b58f32e73202334.jpg");
        book.setSmImage("https://img.thriftbooks.com/api/images/l/50f77ef9553d221575b1e3006b58f32e73202334.jpg");
        bookRepository.saveAll(Flux.just(book)).subscribe();
    }
}
