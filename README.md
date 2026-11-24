# Books Management System

Spring Boot API for a library-style books management system. The first implemented feature is the book catalog. See [REQUIREMENTS.md](REQUIREMENTS.md) for the full development checklist.

## Run locally

Use Java 21 and Maven. Create a MySQL database named `books_management_system`, then set the connection values in `src/main/resources/application.properties` for your local MySQL installation. The current file uses `root` / `root`. Hibernate creates or updates the `books` table at startup.

```sh
mvn spring-boot:run
```

Run the tests with:

```sh
mvn test
```

The tests use an in-memory H2 database, so they do not require MySQL.

## Book API

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/api/books` | Create a book (`201 Created`) |
| `GET` | `/api/books/{id}` | Get a book (`404` if missing) |
| `GET` | `/api/books` | List or search books |
| `PUT` | `/api/books/{id}` | Replace a book's details |
| `DELETE` | `/api/books/{id}` | Delete a book (`204 No Content`) |

For `POST` and `PUT`, send JSON with required `title`, `author`, and `isbn`. You can also include optional `description` (up to 2,000 characters), `publisher` (up to 255 characters), `publicationYear` (a positive number), and `genre` (up to 100 characters). Omitted or blank optional text fields are stored as `null`. `PUT` replaces the full book, so omitting an existing optional field clears it. ISBN-10 and ISBN-13 values are accepted with or without spaces and hyphens; they must have a valid check digit. Stored ISBNs are normalized to digits, or digits plus `X` for ISBN-10.

```sh
curl -i -X POST http://localhost:8080/api/books \
  -H 'Content-Type: application/json' \
  -d '{"title":"Effective Java","author":"Joshua Bloch","isbn":"978-0-13-468599-1","publisher":"Addison-Wesley","publicationYear":2018,"genre":"Programming","description":"Practical Java advice"}'
```

Use `q` to search title, author, and ISBN. Results are paginated; the default page is `0` and default size is `20`. The maximum size is `100`. Sort by `id`, `title`, `author`, `isbn`, `publisher`, `publicationYear`, or `genre`.

```sh
curl 'http://localhost:8080/api/books?q=java&page=0&size=10&sort=title,asc'
```

The list response contains `content`, `page`, `size`, `totalElements`, and `totalPages`. Validation and missing-book errors use `application/problem+json`. ISBN uniqueness and access controls have not been decided or implemented yet.
