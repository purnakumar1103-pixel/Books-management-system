# Books Management System Requirements

This document is the development checklist for a library-style books management system. Update it when a requirement changes or is completed.

## Scope and build order

The system manages book titles, physical copies, library members, and borrowing. Build the core features in this order:

1. Book catalog and search
2. Physical copy inventory
3. Members
4. Borrowing and returns
5. Reservations, overdue tracking, fines, accounts, and reports

Check an item only when the feature is implemented and verified.

## Functional requirements

### Book catalog

- [x] Add a book with its title, author, and ISBN.
- [x] Add optional description, publisher, publication year, and genre to each book.
- [x] View one book and browse the catalog.
- [x] Update and delete a book. Rules for deleting books with loan history remain to be decided.
- [x] Search books by title, author, and ISBN.
- [x] Catalog results support pagination and sorting.

### Physical copies

- [ ] The system records individual physical copies separately from book titles.
- [ ] Staff can add, view, update, and remove copies.
- [ ] Users can see total and available copy counts for a book.
- [ ] A copy's availability reflects its active loan or reservation state.

### Members

- [ ] Staff can register, view, update, and deactivate members.
- [ ] Staff can view a member's current loans and borrowing history.

### Borrowing and returns

- [ ] Staff can loan an available copy to an active member.
- [ ] Each loan records its member, copy, checkout date, due date, and return date when returned.
- [ ] Staff can record a return and make the copy available again, subject to reservations.
- [ ] The system prevents a copy from having more than one active loan.
- [ ] The system rejects loans for unavailable copies or inactive members.

### Additional library features

- [ ] Members can reserve a book when no copy is available.
- [ ] Staff can see overdue loans.
- [ ] The system can calculate and track fines for overdue returns.
- [ ] Staff and members have accounts with permissions appropriate to their roles.
- [ ] Restrict catalog changes to staff accounts.
- [ ] Staff can view reports for available books, active loans, and overdue loans.

## Technical requirements

- [x] Store catalog data in the database.
- [ ] Store copy, member, and loan data in the database.
- [x] Validate book data, including required fields and ISBN format.
- [x] Return clear book API errors with appropriate HTTP status codes.
- [x] Keep book request handling, business rules, and persistence in separate controller, service, and repository layers.
- [x] Test catalog CRUD, search, validation, and missing-book responses.
- [ ] Test borrowing rules, including invalid and conflicting requests.

## Rules to decide before implementing dependent features

- [ ] Decide whether ISBN identifies a book edition and whether it must be unique in the catalog.
- [ ] Decide when a book or copy may be deleted if it has loan history.
- [ ] Decide the loan period, renewal policy, and maximum active loans per member.
- [ ] Decide reservation order, expiry, and how a returned copy is allocated.
- [ ] Decide the fine rate, grace period, maximum fine, and whether unpaid fines block borrowing.
- [ ] Decide which catalog and member actions require staff authentication.

## Current project state

The book catalog CRUD and search API is implemented and tested with H2. The application uses MySQL when run normally. Catalog endpoints are currently open to all callers because authentication has not been implemented. Physical copies, members, loans, and the remaining library features have not been implemented yet.
