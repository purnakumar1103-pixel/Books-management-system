package com.books.books_management_system.book;

public class InvalidBookQueryException extends RuntimeException {

	public InvalidBookQueryException(String message) {
		super(message);
	}
}
