package com.booksearch;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
	Flux<Book> findAll();
}
