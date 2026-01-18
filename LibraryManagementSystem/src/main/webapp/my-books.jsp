<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>My Borrowing History</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<nav class="navbar">
    <div class="logo">📚 Library System</div>
    <ul>
        <li><a href="dashboard">Browse Books</a></li>
        <li><a href="my-books" class="nav-active">My Borrowed Books</a></li>
        <li><a href="notifications">Notifications</a></li>
        <li><a href="auth?action=logout" class="btn btn-sm btn-outline">Logout</a></li>
    </ul>
</nav>

<div class="main-content">
    <h1>My Books</h1>
    <p class="subtitle">Track what you borrowed and manage returns.</p>

    <c:if test="${not empty param.msg}">
        <div class="alert alert-success">${param.msg}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <div class="table-card">
        <table>
            <thead>
            <tr>
                <th>Book Title</th>
                <th>Borrowed On</th>
                <th>Due Date</th>
                <th>Status</th>
                <th style="width: 140px;">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="record" items="${borrowingRecords}">
                <tr>
                    <td>${record.book.title}</td>
                    <td>${record.borrowDate}</td>
                    <td>
                        ${record.dueDate}
                        <c:if test="${record.status != 'RETURNED' && record.dueDate < java.time.LocalDate.now()}">
                            <br><span class="badge-overdue">(Overdue)</span>
                        </c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${record.status == 'RETURNED'}">
                                <span class="badge-returned">Returned</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-active">Active</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${record.status != 'RETURNED'}">
                                <form action="returnBook" method="post">
                                    <input type="hidden" name="recordId" value="${record.id}">
                                    <button type="submit" class="btn-return" onclick="return confirm('Return this book?');">
                                        Return Book
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <small class="text-muted">On: ${record.returnDate}</small>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty borrowingRecords}">
            <div class="empty-state">
                <h3>No borrowing history found.</h3>
                <p>Browse books to start borrowing.</p>
            </div>
        </c:if>
    </div>
</div>

</body>
</html>