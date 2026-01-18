package com.inventory.librarymanagementsystem;

import com.inventory.librarymanagementsystem.Service.BorrowingRecordService;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "BorrowServlet", urlPatterns = {"/borrow"})
public class BorrowServlete extends HttpServlet {

    @Inject
    private BorrowingRecordService borrowingRecordService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String bookIdStr = request.getParameter("bookId");
            if (bookIdStr == null || bookIdStr.isEmpty()) {
                response.sendRedirect("dashboard?error=Missing+Book+ID");
                return;
            }

            Long bookId = Long.parseLong(bookIdStr);
            borrowingRecordService.borrowBook(bookId, user.getId());

            response.sendRedirect("dashboard?msg=" + URLEncoder.encode("Book borrowed successfully", StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | IllegalStateException e) {
            String message = e.getMessage();
            if (message == null || message.isBlank()) {
                message = "Invalid request";
            }
            response.sendRedirect("dashboard?error=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard?error=System+Error");
        }
    }
}

