package com.booksearch;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

/**
 * The Interface BookRepository.
 */
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
	Flux<Book> findAll();
	
	/**
	 * Find first 10 records by author.
	 *
	 * @param author the author
	 * @return the flux
	 */
	@Query("{ 'author': ?0 }")
    Flux<Book> findFirst10ByAuthor(String author);
}
