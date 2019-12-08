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

// TODO: Auto-generated Javadoc
/**
 * The Class BookController.
 */
@Controller
public class BookController {

	private final BookRepository bookRepository;
	private final int NUM_ITEMS_PER_PAGE = 10;

	/**
	 * Instantiates a new book controller.
	 *
	 * @param bookRepository the book repository
	 */
	BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	/**
	 * Routes.
	 *
	 * @param br the br
	 * @return the router function
	 */
	@Bean
	RouterFunction<ServerResponse> routes(BookRepository br) {
		return RouterFunctions.route(GET("/bookfeed"), serverRequest -> ok().body(br.findAll(), Book.class));
	}

	/**
	 * Books.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/books")
	public String books(final Model model) {

		model.addAttribute("books", bookRepository.findFirst10ByAuthor("J.K. Rowling"));

		return "index";
	}

	/**
	 * Search.
	 *
	 * @param model the model
	 * @param term the term
	 * @param pageNum the page num
	 * @return the string
	 * @throws InterruptedException the interrupted exception
	 */
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
       
		// execute the query on the solr 
        // server and get results
        QueryResponse response = null;
        int queryCount = 0;
		try {
			// execute query
			System.out.println("client: " + client);
			System.out.println("response: " + response);
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
        
        // add attributes to model
        model.addAttribute("books", bookList);
        model.addAttribute("range", range);
        model.addAttribute("count", queryCount);
 
		return "search";
	}
}