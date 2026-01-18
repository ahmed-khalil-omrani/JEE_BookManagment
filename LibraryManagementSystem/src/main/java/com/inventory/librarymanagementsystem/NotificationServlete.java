package com.inventory.librarymanagementsystem;

import com.inventory.librarymanagementsystem.Service.NotificationService;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "NotificationServlet", urlPatterns = {"/notifications"})
public class NotificationServlete extends HttpServlet {

    @Inject
    private NotificationService notificationService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.setAttribute("notifications", notificationService.getAllNotificationsForUser(user.getId()));
        request.getRequestDispatcher("notifications.jsp").forward(request, response);
    }
}

