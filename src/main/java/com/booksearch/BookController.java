package com.booksearch;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

@Controller
public class BookController {

	private final BookRepository bookRepository;

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
	public String search(final Model model, @RequestParam(defaultValue = "asdf") String term) throws InterruptedException {
		
		// create booklist to populate
		// search results with
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		// create new solrclient instance
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/bookstore").build();

		// create and configure search query
        SolrQuery query = new SolrQuery();
        query.setQuery(term);
        query.setFields("isbn", "title", "author", "language",
        		"rating", "year", "smImage", "lgImage");
        query.setStart(0);
        query.setRows(10000);
        query.set("defType", "edismax");
        
        QueryResponse response = null;
        
		try {
			// execute query
			response = client.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// get results from query
        SolrDocumentList results = response.getResults();
        for (int i = 0; i < results.size(); ++i) {
            
        	// create new book instance
        	// with result list parameters
            Book book = new Book(
            		String.valueOf(results.get(i).getFieldValue("isbn")),
            		String.valueOf(results.get(i).getFieldValue("title")),
            		String.valueOf(results.get(i).getFieldValue("author")),
            		String.valueOf(results.get(i).getFieldValue("language")),
            		String.valueOf(results.get(i).getFieldValue("rating")),
            		String.valueOf(results.get(i).getFieldValue("year")),
            		String.valueOf(results.get(i).getFieldValue("smImage")),
            		String.valueOf(results.get(i).getFieldValue("lgImage"))
            	);
            // add book to list
            bookList.add(book);
        }
        
        // add book list to model
        model.addAttribute("books", bookList);
 
		return "search";
	}
}