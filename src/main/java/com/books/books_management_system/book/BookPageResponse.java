package com.books.books_management_system.book;

import java.util.List;

import org.springframework.data.domain.Page;

public record BookPageResponse(
		List<BookResponse> content,
		int page,
		int size,
		long totalElements,
		int totalPages) {

	public static BookPageResponse from(Page<BookResponse> books) {
		return new BookPageResponse(books.getContent(), books.getNumber(), books.getSize(),
				books.getTotalElements(), books.getTotalPages());
	}
}
