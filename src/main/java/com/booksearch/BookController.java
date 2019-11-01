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
import java.util.stream.IntStream;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

@Controller
public class BookController {

	private final BookRepository bookRepository;
	private final int NUM_ITEMS_PER_PAGE = 10;

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
	public String search(final Model model, 
			@RequestParam(defaultValue = "asdf") String term,
			@RequestParam(defaultValue = "1") int pageNum) throws InterruptedException {
		
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
        
        // paginate search results
        query.setStart((pageNum - 1) * NUM_ITEMS_PER_PAGE);
     	query.setRows(NUM_ITEMS_PER_PAGE);
     		
        query.set("defType", "edismax");
       
		// execute the query on the server and get results
        
        // TODO: 
        // pagination- 20 records per page
        // Stay with solr, no hadoop
        // more finite search settings
        
        // 1) go for hadoop, see what performance gains, response
        // 2) how fast is search with pagination, page load
        // 3) add advanced search checkboxes if necessary
        // 4) 
        QueryResponse response = null;
        int queryCount = 0;
		try {
			// execute query
			response = client.query(query);
			queryCount = (int) client.query(query).getResults().getNumFound();
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
        
        int[] range = IntStream.rangeClosed(1, queryCount).toArray();
        
        // add book list to model
        model.addAttribute("books", bookList);
        model.addAttribute("range", range);
        model.addAttribute("count", queryCount);
 
		return "search";
	}
}