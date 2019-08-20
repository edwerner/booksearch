package com.booksearch;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.lucene.search.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.cfg.Environment;
import org.hibernate.search.cfg.SearchMapping;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.service.ServiceRegistry;
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
import org.hibernate.ogm.cfg.OgmProperties;
import org.hibernate.ogm.datastore.mongodb.MongoDBDialect;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import reactor.core.publisher.Flux;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class BookController {

	private final BookRepository bookRepository;
	private FullTextSession fullTextSession;
	private Configuration configuration;

	BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;

		configuration = new Configuration()
				.setProperty("hibernate.ogm.datastore.provider", "mongodb")
				.setProperty("hibernate.ogm.datastore.create_database", "true")
				.setProperty("hibernate.ogm.datastore.database", "test")
				.setProperty("hibernate.ogm.mongodb.host", "127.0.0.1")
				.setProperty("hibernate.ogm.mongodb.port", "27017")
				.setProperty("hibernate.ogm.mongodb.username", "")
				.setProperty("hibernate.ogm.mongodb.password", "")
				.setProperty("hibernate.search.default.directory_provider", "filesystem")
				.setProperty("hibernate.search.default.indexBase", "target/lucene/indexes");
//				.addPackage("com.booksearch")
//				.addAnnotatedClass(Book.class);

//		SessionFactory sessionFactory = configuration.buildSessionFactory();
		SessionFactory sessionFactory = buildSessionFactory();

		Session session = sessionFactory.openSession();

		fullTextSession = Search.getFullTextSession(session);

		try {
////			MassIndexer massIndexer = fullTextSession.createIndexer(Book.class);
////			massIndexer.startAndWait();
			fullTextSession.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		session.close();

		// SearchMapping mapping = new SearchMapping();
		// mapping.entity(Book.class).indexed();
	}
	
	private SessionFactory buildSessionFactory() {
	    try {
	        ServiceRegistry serviceRegistry
	            = new StandardServiceRegistryBuilder()
	                .applySettings(configuration.getProperties()).build();
	        configuration.addAnnotatedClass(Book.class);
	        return configuration
	                .buildSessionFactory(serviceRegistry);
	    } catch(Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("There is issue in hibernate util");
	    }
	}

	@Bean
	RouterFunction<ServerResponse> routes(BookRepository br) {
		return RouterFunctions.route(GET("/bookfeed"), serverRequest -> ok().body(br.findAll(), Book.class));
	}

	@RequestMapping(value = "/books")
	public String books(final Model model) {

		System.out.println("************BOOKS**************");

		// IReactiveDataDriverContextVariable books =
		// new ReactiveDataDriverContextVariable(bookRepository.findAll());

		model.addAttribute("books", bookRepository.findFirst10ByAuthor("J.K. Rowling"));

		return "index";
	}

	@RequestMapping(value = "/search")
	public String search(final Model model, @RequestParam("term") String term) throws InterruptedException {

		System.out.println("TERM: " + term);
		QueryBuilder builder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

		Query luceneQuery = builder.keyword().onField("isbn").andField("title").andField("author").andField("language")
				.andField("year").matching(term).createQuery();
		@SuppressWarnings("rawtypes")
		org.hibernate.query.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);

		@SuppressWarnings("unchecked")
		List<Book> results = fullTextQuery.list();

		model.addAttribute("books", results);

		return "search";
	}
}