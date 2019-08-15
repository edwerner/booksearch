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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class BookController {

	private final BookRepository bookRepository;
//	private SessionFactory sessionFactory;

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


		
		System.out.println("************BOOKS**************");
		
		// IReactiveDataDriverContextVariable books =
		// new ReactiveDataDriverContextVariable(bookRepository.findAll());

		model.addAttribute("books", bookRepository.findFirst10ByAuthor("J.K. Rowling"));

		return "index";
	}

	@RequestMapping(value = "/search")
	public String search(final Model model, @RequestParam("term") String term) throws InterruptedException {
		
		System.out.println("TERM: " + term);
		
		
		
		
//		SearchMapping mapping = new SearchMapping();
//		mapping.entity(Book.class).indexed();

		Configuration config = new Configuration();
//		config.getProperties().put(Environment.MODEL_MAPPING, mapping);
				
//		config.setProperty("hibernate.ogm.datastore.database", "test")
//		.addResource("com/booksearch/Book.hbm.xml")
//	    .setProperty("hibernate.ogm.datastore.database", "test")
//	    .setProperty("hibernate.ogm.datastore.provider", "mongodb")
//	    .setProperty("hibernate.ogm.mongodb.host", "127.0.0.1")
//	    .setProperty("hibernate.ogm.mongodb.port", "27017")
//		.setProperty("hibernate.dialect", "org.hibernate.ogm.datastore.mongodb.MongoDBDialect")
////	    .setProperty("hibernate.hbm2ddl.auto", "validate")
//		.setProperty("hibernate.search.default.directory_provider", "filesystem")
//		.setProperty("hibernate.search.default.indexBase", "/var/lucene/indexes")
////	    .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect")
////		.setProperty("entitymanager.packagesToScan", "com.booksearch")
////	    .setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbc.JDBCDriver")
//	    .setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver")
		config.addClass(com.booksearch.Book.class);
		
		
		SessionFactory sessionFactory = config.configure().buildSessionFactory();

		Session session = sessionFactory.openSession();
		
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		fullTextSession.createIndexer().startAndWait();
		

//		QueryBuilder builder = fullTextSession.getSearchFactory()
//				.buildQueryBuilder().forEntity(com.booksearch.Book.class).get();
		
		
		
		
		
		

		
//		   .setProperty("hibernate.ogm.datastore.database", "test")
//		    .setProperty("hibernate.ogm.datastore.provider", "org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider")
//		    .setProperty("mapping.resource", "com.booksearch.Book.hbm.xml")
//			.setProperty("hibernate.search.default.directory_provider", "filesystem")
//			.setProperty("hibernate.search.default.indexBase", "/var/lucene/indexes")
//			.setProperty("hibernate.connection.pool_size", "10")
//			.setProperty("hibernate.ogm.datastore.dialect", "org.hibernate.ogm.datastore.mongodb.MongoDBDialect");
//		    .setProperty("hibernate.ogm.datastore.dialect", "org.hibernate.dialect.HSQLDialect")
//		    .setProperty("hibernate.ogm.mongodb.host", "127.0.0.1")
//		    .setProperty("hibernate.ogm.mongodb.port", "27017")
//		    .setProperty("hibernate.ogm.mongodb.database", "test")
//		    .setProperty("hibernate.ogm.datastore.provider", "mongodb")
//		    .setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:test")
//		    .setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbc.JDBCDriver")
//		    .setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect")
//	        .setProperty("hibernate.ogm.datastore.create_database", "true");
//		    .setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbc.JDBCDriver");
		
		
		
		
		
		
		
		
		
		
		
		
		
//		System.out.println("CONFIG: " + config.buildSessionFactory());
//		SessionFactory sessionFactory = config.buildSessionFactory();
//		Session session = sessionFactory.openSession();
//				
//		FullTextSession fullTextSession = Search.getFullTextSession(session);
//		fullTextSession.createIndexer().startAndWait();
//		
//		
//		Query luceneQuery = builder.keyword()
//				.onField("isbn")
//				.andField("title")
//				.andField("author")
//				.andField("language")
//				.andField("year")
//				.matching(term)
//				.createQuery();
//		
//		@SuppressWarnings("rawtypes")
//		org.hibernate.query.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);
//		
//		@SuppressWarnings("unchecked")
//		List<Book> results = fullTextQuery.list();
//		
//		model.addAttribute("books", results);
		
		return "search";
	}
   
//    public void loadSessionFactory() {
//    	
//    	Properties properties = null;
//        if (properties == null) {
//            properties = new Properties();
//            try {
//                properties.load(PropertiesUtil.class.getResourceAsStream("application.properties"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
// 
//        Configuration configuration = new Configuration();
////        configuration.configure("application.properties");
//	    configuration.setProperty("hibernate.dialect", "org.hibernate.ogm.datastore.mongodb.MongoDBDialect");
//        configuration.addProperties(properties);
//        configuration.addAnnotatedClass(Book.class);
//        ServiceRegistry srvcReg = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//        sessionFactory = configuration.buildSessionFactory(srvcReg);
//    }
 
//    public Session getSession() throws HibernateException {
// 
//        Session retSession = null;
//            try {
//                retSession = sessionFactory.openSession();
//            }catch(Throwable t){
//	            System.err.println("Exception while getting session.. ");
//	            t.printStackTrace();
//            }
//            if(retSession == null) {
//                System.err.println("session is discovered null");
//            }
//            return retSession;
//    }
}