# Library Management System

Online Library Management System built with Jakarta EE (Servlets/JSP/JSTL/JSF) and JPA. Users can browse and search books, borrow/return items, view history, and receive notifications. Librarians manage inventory and overdue tracking.

## Project Overview
- Browse books and search by title/author/ISBN/category.
- View book details, availability, and borrow with limits.
- Manage borrowed books, returns, and view borrowing history.
- In-app notifications for due/overdue and success events.
- Admin/Librarian inventory management (add/edit/delete).
- REST API for external/mobile integration.
- Modern UI (card-based catalog, dark theme) with search bar in dashboard.

## Tech Stack
- Jakarta EE: Servlets, JSP, JSTL/EL, (admin panel), CDI.
- JPA/Hibernate with `LibraryPU`.
- REST: JAX-RS resources under `/api`.
- Frontend: JSP pages + shared `style.css`.
- Build/Run: Maven-compatible app server (e.g., TomEE / Payara / WildFly).

## Key Modules (selected)
- Servlets: `DashboardServlet` (search + catalog), `BorrowServlete`, `MyBooksServlet`, `NotificationServlete`, `LoginServlet`, `RegisterServlet`, `DeleteBookServlete`.
- Services: `BookService`, `BorrowingRecordService`, `NotificationService`, `UserService`.
- REST (JAX-RS): `BookResources`, `BorrowingResources`, `NotificationResources`, `UsersResources`.
- Entities: `Book`, `User`, `BorrowingRecord`, `Notification`.

## Database Schema (conceptual)
- `Book(id, title, author, isbn, publishDate, totalCopies, availableCopies, category)`
- `User(id, name, email, membershipDate, membershipType)`
- `BorrowingRecord(id, user_id, book_id, borrowDate, dueDate, returnDate, status)`
- `Notification(id, user_id, message, type, isRead, createdDate)`

## REST API (outline)
- `GET /api/books` — Search/list books (supports pagination & filters).
- `GET /api/books/{id}` — Book details.
- `POST /api/books` — Add book (admin).
- `PUT /api/books/{id}` — Update book (admin).
- `DELETE /api/books/{id}` — Delete book (admin).
- `POST /api/books/{id}/return` — Return a book.
- `POST /api/borrowing` — Borrow a book.
- `GET /api/borrowing/history` — Current user borrowing history.
- `GET /api/notifications` — List user notifications.
- `GET /api/users` — List users (admin).
- `GET /api/users/{id}` — User details.

## Web UX
- `dashboard.jsp` — Catalog with search bar (by title) and borrow actions; admin delete button when applicable.
- `my-books.jsp` — Borrowing history + returns.
- `notifications.jsp` — Notification center.
- Auth pages: `login.jsp`, `index.jsp`; admin JSF views for inventory.

## Running the Project
1) Ensure Java 17+ and Maven are installed.  
2) Configure your Jakarta EE app server with `LibraryPU` datasource.  
3) Build and deploy: `mvn clean package` then deploy the WAR to the server.  
4) Access via browser: `/` (login), `/dashboard`, `/my-books`, `/notifications`.  
5) REST base path: `/api/*`.

## Borrowing Rules (expected)
- Borrow limit: 5 books per user.
- Due date default: 2 weeks; overdue tracked and notified.
- Returning increments `availableCopies`.

## Tests & Documentation
- Add test cases for service and REST layers (not included in repo).
- Provide API docs (Swagger/Postman) and DB init script per deliverables.
- Capture UI screenshots for submission.

## Contributions / Next Steps
- Wire JSF admin forms to service layer validations.
- Add pagination/filtering to dashboard search.
- Enhance security (strong auth, input validation, role checks).

