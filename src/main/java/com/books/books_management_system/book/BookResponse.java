package com.books.books_management_system.book;

public record BookResponse(Long id, String title, String author, String isbn,
		String description, String publisher, Integer publicationYear, String genre) {

	public static BookResponse from(Book book) {
		return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
				book.getDescription(), book.getPublisher(), book.getPublicationYear(), book.getGenre());
	}
}
