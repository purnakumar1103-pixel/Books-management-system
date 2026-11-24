package com.books.books_management_system.book;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private final BookService service;

	public BookController(BookService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
		BookResponse book = service.create(request);
		return ResponseEntity.created(URI.create("/api/books/" + book.id())).body(book);
	}

	@GetMapping("/{id}")
	public BookResponse get(@PathVariable Long id) {
		return service.get(id);
	}

	@GetMapping
	public BookPageResponse list(@RequestParam(required = false) String q,
			@PageableDefault(size = 20, sort = "id") Pageable pageable) {
		return BookPageResponse.from(service.list(q, pageable));
	}

	@PutMapping("/{id}")
	public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
		return service.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
