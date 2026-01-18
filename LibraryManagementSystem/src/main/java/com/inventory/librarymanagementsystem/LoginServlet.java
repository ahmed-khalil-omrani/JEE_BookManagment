package com.inventory.librarymanagementsystem;

import com.inventory.librarymanagementsystem.Service.UserService;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet(name = "LoginServlet", urlPatterns = {"/auth"})
public class LoginServlet extends HttpServlet {

    @Inject
    private UserService userService;

    // --- 1. HANDLE LOGIN SUBMISSION (POST) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {

            User user = userService.Login(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("loggedUser", user);

                response.sendRedirect(request.getContextPath() + "/dashboard");

            } else {

                request.setAttribute("errorMessage", "Invalid email or password");

                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {

            e.printStackTrace();
            request.setAttribute("errorMessage", "System Error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp?logout=true");
        }
        else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}