package com.booksearch;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
	Flux<Book> findAll();
	@Query("{ 'author': ?0 }")
    Flux<Book> findFirst10ByAuthor(String author);
}
