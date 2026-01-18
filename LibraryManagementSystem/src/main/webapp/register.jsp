<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Register - Library System</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="center-container">
    <div class="auth-card">
        <h2>Create Account</h2>

        <form action="register" method="post">
            <label>Full Name</label>
            <input type="text" name="name" placeholder="John Doe" required>

            <label>Email</label>
            <input type="email" name="email" placeholder="john@example.com" required>

            <label>Password</label>
            <input type="password" name="password" placeholder="••••••••" required>

            <label>Role</label>
            <select name="role">
                <option value="MEMBER">Member</option>
                <option value="ADMIN">Admin</option>
            </select>

            <button type="submit">Register Now</button>
        </form>

        <p class="auth-footer">
            Already have an account? <a href="login.jsp">Login here</a>
        </p>
    </div>
</div>

</body>
</html>