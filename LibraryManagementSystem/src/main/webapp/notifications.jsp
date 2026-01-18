<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Notifications</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .notif-card {
            background: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 18px;
            display: flex;
            gap: 14px;
            align-items: flex-start;
        }
        .notif-badge {
            padding: 6px 12px;
            border-radius: 999px;
            font-size: 0.85rem;
            font-weight: 700;
            display: inline-block;
        }
        .type-success { background: rgba(76,175,80,0.18); color: #8be198; border: 1px solid #4caf50; }
        .type-due { background: rgba(255,193,7,0.14); color: #ffda6a; border: 1px solid #ffc107; }
        .type-overdue { background: rgba(233,19,19,0.15); color: #ff8080; border: 1px solid var(--accent-red); }
        .notif-time { color: var(--text-muted); font-size: 0.9rem; }
        .notif-message { color: var(--text-main); margin: 6px 0 0; }
        .empty-state { text-align: center; color: var(--text-muted); padding: 40px 0; }
        .list-stack { display: flex; flex-direction: column; gap: 12px; margin-top: 16px; }
    </style>
</head>
<body>

<nav class="navbar">
    <div class="logo">📚 Library System</div>
    <ul>
        <li><a href="dashboard">Browse Books</a></li>
        <li><a href="my-books">My Borrowed Books</a></li>
        <li><a href="notifications" style="color: var(--accent-red);">Notifications</a></li>
        <li><a href="auth?action=logout" style="border: 1px solid #444; padding: 5px 10px; border-radius: 4px;">Logout</a></li>
    </ul>
</nav>

<div class="main-content">
    <h1>Notifications</h1>
    <p style="color: var(--text-muted); margin-top: -10px;">Latest updates about your loans.</p>

    <c:if test="${not empty param.msg}">
        <div class="alert alert-success">${param.msg}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <div class="list-stack">
        <c:forEach var="notif" items="${notifications}">
            <div class="notif-card">
                <div style="flex: 1;">
                    <div>
                        <c:choose>
                            <c:when test="${notif.type == 'SUCCESS'}">
                                <span class="notif-badge type-success">Success</span>
                            </c:when>
                            <c:when test="${notif.type == 'DUE_REMINDER'}">
                                <span class="notif-badge type-due">Due Reminder</span>
                            </c:when>
                            <c:when test="${notif.type == 'OVERDUE_ALERT'}">
                                <span class="notif-badge type-overdue">Overdue Alert</span>
                            </c:when>
                        </c:choose>
                        <span class="notif-time">${notif.createdDate}</span>
                    </div>
                    <div class="notif-message">${notif.message}</div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty notifications}">
            <div class="empty-state">
                <h3>No notifications yet.</h3>
                <p>We'll let you know about due dates and returns here.</p>
            </div>
        </c:if>
    </div>
</div>

</body>
</html>

