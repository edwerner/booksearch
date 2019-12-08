package com.booksearch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

import reactor.core.publisher.Flux;

/**
 * The Class CSVReader.
 */
public class CSVReader {
	private BookRepository bookRepository;
	private String url = "http://localhost:8983/solr/bookstore";
	private HttpSolrClient solrClient;
	@SuppressWarnings("unused")
	private SolrInputDocument document;

	/**
	 * Instantiates a new CSV reader.
	 *
	 * @param bookRepository the book repository
	 */
	public CSVReader(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
		solrClient = new HttpSolrClient.Builder(url).build();
		solrClient.setParser(new XMLResponseParser());
		document = new SolrInputDocument();
	}
	
	/**
	 * Save books.
	 *
	 * @throws SolrServerException the solr server exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void saveBooks() throws SolrServerException, IOException {
		String csvFile = "books.csv";
		BufferedReader br = null;
		String line = "";
		
		String isbn = "";
		String author = "";
		String year = "";
		String title = "";
		String language = "";
		String rating = "";
		String lgImage = "";
		String smImage = "";
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				String[] books = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				isbn = books[5];
				author = books[7];
				year = books[8].replaceAll("\\.0*$", "");
				title = books[10];
				language = books[11];
				rating = books[12];
				
				// regex for actual image url
				String isBookImage = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				lgImage = books[21];
				smImage = books[22];
				
				// string match image url
				boolean isLgUrl = Pattern.matches(isBookImage, lgImage);
				boolean isSmUrl = Pattern.matches(isBookImage, smImage);
				
				// replace missing url with placeholder image
				if (isLgUrl) {
					lgImage = books[21];
				} else {
					lgImage = "https://s.gr-assets.com/assets/nophoto/book/50x75-a91bf249278a81aabab721ef782c4a74.png";
				}
				
				if (isSmUrl) {
					smImage = books[22];
				} else {
					smImage = "https://s.gr-assets.com/assets/nophoto/book/50x75-a91bf249278a81aabab721ef782c4a74.png";
				}
				
				// create new book instance
				Book book = new Book(isbn, title, author, language, rating, year, lgImage, smImage);
				
				// save book to mongodb
				bookRepository.saveAll(Flux.just(book)).subscribe();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// index books to solr server
//				indexBooks();
			}
		}
	}

	/**
	 * Index books to solr server.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SolrServerException the solr server exception
	 */
	public void indexBooks() throws IOException, SolrServerException { 
		
		Flux<Book> books = bookRepository.findAll();
		List<Book> bookList = books.collectList().block();
		
		for (Book book : bookList) {
			solrClient.addBean(book);
		}
	}
}