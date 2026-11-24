package com.books.books_management_system.book;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class BookControllerTests {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private BookRepository repository;

	private MockMvc mvc;

	@BeforeEach
	void setUp() {
		repository.deleteAll();
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void createsReadsUpdatesAndDeletesBook() throws Exception {
		String location = mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"title":"  Effective Java  ","author":" Joshua Bloch ","isbn":"978-0-13-468599-1"}
						"""))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.title").value("Effective Java"))
				.andExpect(jsonPath("$.isbn").value("9780134685991"))
				.andReturn().getResponse().getHeader("Location");

		mvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.author").value("Joshua Bloch"));

		mvc.perform(put(location)
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
							{"title":"Effective Java, Third Edition","author":"Joshua Bloch","isbn":"9780134685991"}
							"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Effective Java, Third Edition"));

		mvc.perform(delete(location)).andExpect(status().isNoContent());
		mvc.perform(get(location))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.detail").exists());
	}

	@Test
	void searchesAndPaginatesBooks() throws Exception {
		repository.save(new Book("Clean Code", "Robert Martin", "9780132350884"));
		repository.save(new Book("Effective Java", "Joshua Bloch", "9780134685991"));

		mvc.perform(get("/api/books").param("q", "effective"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(1))
				.andExpect(jsonPath("$.content[0].title").value("Effective Java"));

		mvc.perform(get("/api/books").param("q", "MARTIN"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].title").value("Clean Code"));

		mvc.perform(get("/api/books").param("q", "978-0-13-468599-1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].title").value("Effective Java"));

		mvc.perform(get("/api/books").param("page", "0").param("size", "1").param("sort", "title,asc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(2))
				.andExpect(jsonPath("$.content[0].title").value("Clean Code"));

		mvc.perform(get("/api/books").param("q", "---"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value(0));

		mvc.perform(get("/api/books").param("sort", "unknown"))
				.andExpect(status().isBadRequest());

		mvc.perform(get("/api/books").param("size", "101"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void rejectsInvalidBookData() throws Exception {
		mvc.perform(post("/api/books")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
							{"title":" ","author":"Someone","isbn":"123"}
							"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("Invalid book data"));

		mvc.perform(post("/api/books")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""
							{"title":"A Book","author":"Someone","isbn":"---"}
							"""))
				.andExpect(status().isBadRequest());
	}
}
