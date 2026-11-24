package com.books.books_management_system.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String author;

	@Column(nullable = false, length = 13)
	private String isbn;

	@Column(length = 2000)
	private String description;

	private String publisher;

	private Integer publicationYear;

	@Column(length = 100)
	private String genre;

	protected Book() {
	}

	public Book(String title, String author, String isbn) {
		this(title, author, isbn, null, null, null, null);
	}

	public Book(String title, String author, String isbn, String description, String publisher,
			Integer publicationYear, String genre) {
		update(title, author, isbn, description, publisher, publicationYear, genre);
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getDescription() {
		return description;
	}

	public String getPublisher() {
		return publisher;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public String getGenre() {
		return genre;
	}

	public void update(String title, String author, String isbn, String description, String publisher,
			Integer publicationYear, String genre) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.description = description;
		this.publisher = publisher;
		this.publicationYear = publicationYear;
		this.genre = genre;
	}
}
