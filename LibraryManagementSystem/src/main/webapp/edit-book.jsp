<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Book</title>
    <link rel="stylesheet" href="style.css">
    <style>
        /* Existing styles */
        .form-card {
            background: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 24px;
            max-width: 640px;
            margin: 16px auto;
            box-shadow: 0 10px 30px rgba(0,0,0,0.35);
            display: flex;
            flex-direction: column;
            gap: 14px;
        }
        .form-row { display: flex; gap: 12px; }
        .form-row > div { flex: 1; }
        .hint { color: var(--text-muted); font-size: 0.9rem; margin-top: 4px; }

        /* IMPROVEMENT: Ensure actions are aligned at the start and have top margin */
        .actions {
            display: flex;
            gap: 10px;
            align-items: center;
            margin-top: 10px; /* Add some space above the buttons */
            justify-content: flex-start; /* Ensure buttons start at the left */
        }
    </style>
</head>
<body>

<nav class="navbar">
    <div class="logo">📚 Library System</div>
    <ul>
        <li><a href="dashboard">Browse Books</a></li>
        <li><a href="my-books">My Borrowed Books</a></li>
        <li><a href="notifications">Notifications</a></li>
        <li><a href="auth?action=logout" style="border: 1px solid #444; padding: 5px 10px; border-radius: 4px;">Logout</a></li>
    </ul>
</nav>

<div class="main-content">
    <h1>Edit Book</h1>
    <p style="color: var(--text-muted); margin-top: -10px;">Update book details (admin only).</p>

    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success">${param.msg}</div>
    </c:if>

    <div class="form-card">
        <form action="editBook" method="post">
            <input type="hidden" name="id" value="${book.id}">

            <label for="title">Title</label>
            <input type="text" id="title" name="title" value="${book.title}" required>

            <label for="author">Author</label>
            <input type="text" id="author" name="author" value="${book.author}" required>

            <label for="category">Category</label>
            <input type="text" id="category" name="category" value="${book.category}">

            <div class="form-row">
                <div>
                    <label for="totalCopies">Total Copies</label>
                    <input type="number" id="totalCopies" name="totalCopies" value="${book.totalCopies}" min="1" required>
                </div>
                <div>
                    <label for="availableCopies">Available Copies</label>
                    <input type="number" id="availableCopies" name="availableCopies" value="${book.availableCopies}" min="0" required>
                    <div class="hint">Must be ≤ total copies.</div>
                </div>
            </div>

            <div class="actions">
                <button type="submit" class="btn btn-primary" style="width:auto;">Save Changes</button>
                <a href="dashboard" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>

