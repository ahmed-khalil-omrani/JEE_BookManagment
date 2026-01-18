<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Use Jakarta URI for Tomcat 10+ / TomEE --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Dashboard - Library</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<nav class="navbar">
    <div class="logo">📚 Library System</div>
    <ul>
        <li><a href="dashboard" class="nav-active">Browse Books</a></li>
        <li><a href="my-books">My Borrowed Books</a></li>
        <li><a href="notifications">Notifications</a></li>
        <li><a href="auth?action=logout" class="btn btn-sm btn-outline">Logout</a></li>
    </ul>
</nav>

<div class="main-content">
    <div class="header-section">
        <div class="header-title">
            <h1>Available Books</h1>
            <p class="subtitle">Welcome, <strong>${sessionScope.loggedUser.name}</strong>. Here is our collection.</p>
        </div>
        <div class="header-actions">
            <form action="dashboard" method="get" class="search-form">
                <input
                    type="text"
                    name="q"
                    placeholder="Search by title..."
                />
                <button type="submit" class="btn btn-primary btn-search">Search</button>
            </form>
            <c:if test="${sessionScope.loggedUser.membershipType == 'ADMIN'}">
                <a href="addBook" class="btn btn-secondary">Add Book</a>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty param.msg}">
        <div class="alert alert-success">${param.msg}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <div class="book-grid">
        <c:forEach var="book" items="${bookList}">
            <div class="book-card">

                <div class="book-cover-container">
                    <c:choose>
                        <c:when test="${not empty book.imageUrl}">
                            <img src="${book.imageUrl}" alt="${book.title}" class="book-cover"
                                 onerror="this.onerror=null;this.src='https://placehold.co/200x300/333/white?text=No+Cover';">
                        </c:when>
                        <c:otherwise>
                            <img src="https://placehold.co/200x300/333/white?text=No+Image" alt="Default" class="book-cover">
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="book-details">
                    <div class="book-title">${book.title}</div>

                    <div class="book-meta">
                        by ${book.author}
                        <c:if test="${not empty book.category}">
                            <span class="category-tag">${book.category}</span>
                        </c:if>
                    </div>

                    <div class="book-meta">ISBN: ${book.isbn}</div>
                    <div class="book-meta meta-sm">Published: ${book.publishDate}</div>

                    <c:choose>
                        <c:when test="${book.availableCopies > 0}">
                            <span class="stock-badge">Available: ${book.availableCopies} / ${book.totalCopies}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="stock-badge out-of-stock">Out of Stock</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <form action="borrow" method="post" class="action-form">
                    <input type="hidden" name="bookId" value="${book.id}">
                    <input type="hidden" name="userId" value="${sessionScope.loggedUser.id}">

                    <c:choose>
                        <c:when test="${book.availableCopies > 0}">
                            <button type="submit" class="btn btn-primary btn-block">
                                Borrow
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" disabled class="btn btn-disabled btn-block">
                                Unavailable
                            </button>
                        </c:otherwise>
                    </c:choose>
                </form>

                <c:if test="${sessionScope.loggedUser.membershipType == 'ADMIN'}">
                    <div class="admin-actions">
                        <form action="editBook" method="get">
                            <input type="hidden" name="id" value="${book.id}">
                            <button type="submit" class="btn btn-secondary btn-block">
                                Edit Book
                            </button>
                        </form>
                        <form action="deleteBook" method="post">
                            <input type="hidden" name="id" value="${book.id}">
                            <input type="hidden" name="userId" value="${sessionScope.loggedUser.id}">
                            <button type="submit" class="btn btn-secondary btn-block">
                                Delete Book
                            </button>
                        </form>
                    </div>
                </c:if>

            </div>
        </c:forEach>

        <c:if test="${empty bookList}">
             <div class="empty-message">No books found in the library.</div>
        </c:if>
    </div>
</div>

</body>
</html>