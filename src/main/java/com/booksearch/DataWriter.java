package com.booksearch;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataWriter implements ApplicationRunner {
    @SuppressWarnings("unused")
	private final BookRepository bookRepository;

    DataWriter(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//    	CSVReader reader = new CSVReader(bookRepository);
//    	reader.saveBooks();
    }
}