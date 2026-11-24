package com.books.books_management_system.book;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

	private static final Set<String> SORT_FIELDS = Set.of("id", "title", "author", "isbn");

	private final BookRepository repository;

	public BookService(BookRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public BookResponse create(BookRequest request) {
		Book book = new Book(request.title().trim(), request.author().trim(), request.normalizedIsbn());
		return BookResponse.from(repository.save(book));
	}

	@Transactional(readOnly = true)
	public BookResponse get(Long id) {
		return BookResponse.from(findBook(id));
	}

	@Transactional(readOnly = true)
	public Page<BookResponse> list(String query, Pageable pageable) {
		if (pageable.getPageSize() > 100) {
			throw new InvalidBookQueryException("Page size must be at most 100");
		}
		pageable.getSort().forEach(order -> {
			if (!SORT_FIELDS.contains(order.getProperty())) {
				throw new InvalidBookQueryException("Unsupported sort field: " + order.getProperty());
			}
		});
		String search = query == null ? null : query.trim();
		String isbnSearch = search == null ? null : search.replaceAll("[\\s-]", "");
		if (isbnSearch != null && isbnSearch.isEmpty()) {
			isbnSearch = search;
		}
		Page<Book> books = search == null || search.isBlank()
				? repository.findAll(pageable)
				: repository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(
						search, search, isbnSearch, pageable);
		return books.map(BookResponse::from);
	}

	@Transactional
	public BookResponse update(Long id, BookRequest request) {
		Book book = findBook(id);
		book.update(request.title().trim(), request.author().trim(), request.normalizedIsbn());
		return BookResponse.from(book);
	}

	@Transactional
	public void delete(Long id) {
		repository.delete(findBook(id));
	}

	private Book findBook(Long id) {
		return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
	}
}
