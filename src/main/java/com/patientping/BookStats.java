package com.patientping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookStats {
	public static void main(String[] args) throws IOException {
		BookStats bookStats = new BookStats();
		bookStats.run();
	}

	public List<Book> loadCsv(InputStream csvInput) throws IOException {
		List<Book> books = new ArrayList<>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(csvInput));
		// To avoid running the while loop on the first row with headers
		reader.readLine();
		String line;
		while ((line = reader.readLine()) != null) {
			// Writes all the values from a row into string array separating each of them by using comma
			String[] parts = line.split(",");
			// Skips the current iteration if the number of values in the row are not equal to 10
			if (parts.length != 10) {
				continue;
			}
			Book b = new Book(Integer.parseInt(parts[0]), parts[1], parts[2], Float.parseFloat(parts[3]), parts[4], parts[5], parts[6], Integer.parseInt(parts[7]), Integer.parseInt(parts[8]), Integer.parseInt(parts[9]));
			books.add(b);
		}
		return books;
	}

	public List<Book> filterToEnglishBooks(List<Book> books) {
		List<Book> englishBooks = new ArrayList<>();
		for (Book b : books) {
			// Collects all the records with language type eng, en-US, en-CA, and en-GB into a list
			// Can just use the contains("en") method if we know that there are no other language codes with the sub-string "en"
			if (b.getLanguage().equalsIgnoreCase("eng") || b.getLanguage().equalsIgnoreCase("en-US") || b.getLanguage().equalsIgnoreCase("en-CA") || b.getLanguage().equalsIgnoreCase("en-GB")) {
				englishBooks.add(b);
			}
		}
		return englishBooks;
	}

	public Map<String, List<Book>> indexByAuthor(List<Book> books) {
		Map<String, List<Book>> booksByAuthor = new HashMap<>();
		for (Book b : books) {
			String[] authors = b.getAuthors().split("-");
			for (String author : authors) {
				// Checks if the author is already added to the HashMap and adds it otherwise
				if (!booksByAuthor.containsKey(author)) {
					booksByAuthor.put(author, new ArrayList<>());
				}
				// Adds the book details to the values list of the author key
				booksByAuthor.get(author).add(b);
			}
		}
		return booksByAuthor;
	}

	public void findMostEnglishBooks(Map<String, List<Book>> booksByAuthor) {
		int mostCount = 0;
		String mostAuthor = null;
		// Checks the size of the value list for each entry in the HashMap and compares it to previous count to find the maximum count
		for (Map.Entry<String, List<Book>> entry : booksByAuthor.entrySet()) {
			int count = entry.getValue().size();
			if (count > mostCount) {
				mostCount = count;
				mostAuthor = entry.getKey();
			}
		}
		System.out.println(String.format("Author with the most english books: %s. %d books.", mostAuthor, mostCount));
	}

	public void findHighestRatedBook(List<Book> books) {
		float highestRating = 0f;
		String highestRatedAuthor = null;
		// Iterates through all the records in the list with more than 24 ratings to find the highest average rated book
		for (Book b : books) {
			if (b.getRatingCount() < 25) {
				continue;
			}
			float rating = b.getAverageRating();
			if (rating > highestRating) {
				highestRating = rating;
				highestRatedAuthor = b.getAuthors();
			}
		}
		System.out.println(String.format("Author with the highest rating: %s. %f stars.", highestRatedAuthor, highestRating));
	}

	public void findHighestRatio(List<Book> books) {
		float highestRatio = 0f;
		String highestRatioAuthor = null;
		// Iterates through all the records in the list with more than 19 ratings and text reviews count to find the highest average rated book
		for (Book b : books) {
			if (b.getRatingCount() < 20 || b.getTextReviewsCount() < 20) {
				continue;
			}
			float ratio = ((float) b.getRatingCount()) / ((float) b.getTextReviewsCount());

			if (ratio > highestRatio) {
				highestRatio = ratio;
				highestRatioAuthor = b.getAuthors();
			}
		}
		System.out.println(String.format("Author with the highest ratio of star reviews to text reviews: %s. %f", highestRatioAuthor, highestRatio));

	}

	public void run() throws IOException {
		InputStream csvInput = new FileInputStream("./src/main/resources/books.csv");

		// Loads the csv file into a list
		List<Book> books = loadCsv(csvInput);
		System.out.println("Loaded " + books.size() + " books");

		// Filters the english books and returns a list
		List<Book> englishBooks = filterToEnglishBooks(books);
		System.out.println("" + englishBooks.size() + " Books in English");

		// Returns a map of records with author as key and the list of books by the author as value
		Map<String, List<Book>> booksByAuthor = indexByAuthor(englishBooks);
		// Prints the record details with maximum value list size in the map
		findMostEnglishBooks(booksByAuthor);

		// Prints the author with the highest average rated english book excluding the ratings that are less than 25
		findHighestRatedBook(englishBooks);

		// Prints the author with the highest average rating across all the books excluding the ratings that are less than 25
		findHighestRatedBook(books);

		// Prints the author of the book with the highest ratio of text reviews to star reviews on their books and the ratio
		findHighestRatio(books);
	}
}
