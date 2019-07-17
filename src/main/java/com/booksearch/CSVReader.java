package com.booksearch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import reactor.core.publisher.Flux;

public class CSVReader {

	public CSVReader() {}
	
	public static void saveBooks(BookRepository bookRepository) {
		String csvFile = "books.csv";
		BufferedReader br = null;
		String line = "";
//		String splitOn = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

//				String[] books = line.split(splitOn);
				

				String[] books = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				String isbn = books[5];
				String author = books[7];
				String year = books[8];
				String title = books[10];
				String language = books[11];
				String rating = books[12];
				
				// regex for actual image url
				String isBookImage = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				String lgImage = books[21];
				String smImage = books[22];

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
				
				Book book = new Book();
				book.setTitle(title);
				book.setAuthor(author);
				book.setIsbn(isbn);
				book.setLanguage(language);
				book.setRating(rating);
				book.setYear(year);
				book.setLgImage(lgImage);
				book.setSmImage(smImage);
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
			}
		}
	}
}