package com.booksearch;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.lucene.search.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
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
import org.hibernate.ogm.datastore.mongodb.MongoDBDialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class BookController {

	private final BookRepository bookRepository;
	private SessionFactory sessionFactory;

	BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
//        loadSessionFactory();
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
	public String search(final Model model, @RequestParam("term") String term) throws InterruptedException {
		
		Configuration cfg = new Configuration()
	    .addClass(com.booksearch.Book.class)
	    .setProperty("hibernate.ogm.datastore.provider", "org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider")
//	    .setProperty("hibernate.ogm.datastore.grid_dialect", "org.hibernate.ogm.datastore.mongodb.MongoDBDialect")
	    .setProperty("hibernate.ogm.datastore.database", "test")
	    .setProperty("hibernate.ogm.mongodb.host", "127.0.0.1")
	    .setProperty("hibernate.ogm.mongodb.port", "27017")
	    .setProperty("hibernate.ogm.mongodb.database", "test")
	    .setProperty("hibernate.ogm.datastore.provider", "mongodb")
	    .setProperty("mapping.resource", "com.booksearch.Book.hbm.xml")
	    .setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect")
	    .setProperty("hibernate.connection.driver_class", "org.hibernate.engine.jdbc.env.spi.JdbcEnvironment");
		SessionFactory sessions = cfg.buildSessionFactory();
		Session session = sessions.openSession();
		
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		fullTextSession.createIndexer().startAndWait();
		
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
   
    public void loadSessionFactory() {
    	
    	Properties properties = null;
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(PropertiesUtil.class.getResourceAsStream("application.properties"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 
        Configuration configuration = new Configuration();
//        configuration.configure("application.properties");
	    configuration.setProperty("hibernate.dialect", "org.hibernate.ogm.datastore.mongodb.MongoDBDialect");
        configuration.addProperties(properties);
        configuration.addAnnotatedClass(Book.class);
        ServiceRegistry srvcReg = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(srvcReg);
    }
 
    public Session getSession() throws HibernateException {
 
        Session retSession = null;
            try {
                retSession = sessionFactory.openSession();
            }catch(Throwable t){
	            System.err.println("Exception while getting session.. ");
	            t.printStackTrace();
            }
            if(retSession == null) {
                System.err.println("session is discovered null");
            }
            return retSession;
    }
}