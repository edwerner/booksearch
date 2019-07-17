package com.booksearch;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	private SessionFactory sessionFactory;

	BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Bean
	RouterFunction<ServerResponse> routes(BookRepository br) {
		return RouterFunctions.route(GET("/bookfeed"), serverRequest -> ok().body(br.findAll(), Book.class));
	}

	@RequestMapping(value = "/books")
	public String books(final Model model) {

		// IReactiveDataDriverContextVariable books =
		// new ReactiveDataDriverContextVariable(bookRepository.findAll());

		model.addAttribute("books", bookRepository.findFirst10ByAuthor("J.K. Rowling"));

		return "index";
	}

	@RequestMapping(value = "/search")
	public String search(final Model model, @RequestParam("term") String term) {
	
		Session session = sessionFactory.openSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		QueryBuilder builder = fullTextSession.getSearchFactory()
				.buildQueryBuilder().forEntity(Book.class).get();
		
		Query luceneQuery = builder.keyword()
				.onField("isbn")
				.andField("title")
				.andField("author")
				.andField("language")
				.andField("year")
				.matching(term)
				.createQuery();
		
		@SuppressWarnings("rawtypes")
		org.hibernate.query.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);
		
		@SuppressWarnings("unchecked")
		List<Book> results = fullTextQuery.list();
		
		model.addAttribute("books", results);
		
		return "search";
	}
}