<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Use 'jakarta.tags.core' for Tomcat 10+ / TomEE --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login - Library System</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="center-container">
    <div class="auth-card">
        <h2>Welcome Back</h2>

        <c:choose>
            <c:when test="${not empty sessionScope.loggedUser}">
                <div class="alert alert-success">
                    You are already logged in as <strong>${sessionScope.loggedUser.name}</strong>.
                </div>

                <a href="dashboard.jsp" class="btn btn-primary" style="display: block; text-align: center;">
                    Go to Dashboard
                </a>

                <div class="auth-footer">
                    <a href="auth?action=logout" class="link-subtle">Logout</a>
                </div>
            </c:when>

            <c:otherwise>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error">${errorMessage}</div>
                </c:if>
                <form action="auth" method="post">
                    <label>Email</label>
                    <input type="email" name="email" required>

                    <label>Password</label>
                    <input type="password" name="password" required>

                    <button type="submit">Login</button>
                </form>

                <p class="auth-footer">
                    New here? <a href="register.jsp">Create an account</a>
                </p>
            </c:otherwise>
        </c:choose>

    </div>
</div>

</body>
</html>