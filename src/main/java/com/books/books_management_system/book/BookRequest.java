package com.books.books_management_system.book;

import java.util.Locale;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
		@NotBlank @Size(max = 255) String title,
		@NotBlank @Size(max = 255) String author,
		@NotBlank @Size(max = 32) String isbn) {

	public String normalizedIsbn() {
		return isbn == null ? null : isbn.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
	}

	@AssertTrue(message = "ISBN must be a valid ISBN-10 or ISBN-13")
	public boolean isValidIsbn() {
		String value = normalizedIsbn();
		if (isbn == null || isbn.isBlank()) {
			return true; // @NotBlank reports missing values.
		}
		if (value.isEmpty()) {
			return false;
		}
		if (value.matches("\\d{9}[\\dX]")) {
			int sum = 0;
			for (int i = 0; i < 9; i++) {
				sum += (10 - i) * Character.digit(value.charAt(i), 10);
			}
			sum += value.charAt(9) == 'X' ? 10 : Character.digit(value.charAt(9), 10);
			return sum % 11 == 0;
		}
		if (value.matches("\\d{13}")) {
			int sum = 0;
			for (int i = 0; i < 13; i++) {
				sum += Character.digit(value.charAt(i), 10) * (i % 2 == 0 ? 1 : 3);
			}
			return sum % 10 == 0;
		}
		return false;
	}
}
