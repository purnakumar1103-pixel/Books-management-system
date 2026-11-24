package com.books.books_management_system.book;

public class BookNotFoundException extends RuntimeException {

	public BookNotFoundException(Long id) {
		super("Book " + id + " was not found");
	}
}
