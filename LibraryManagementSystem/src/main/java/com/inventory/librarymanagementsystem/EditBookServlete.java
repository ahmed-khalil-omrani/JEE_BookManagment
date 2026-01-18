package com.inventory.librarymanagementsystem;

import com.inventory.librarymanagementsystem.Service.BookService;
import com.inventory.librarymanagementsystem.entities.Book;
import com.inventory.librarymanagementsystem.entities.User;
import com.inventory.librarymanagementsystem.enums.MembershipType;
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

@WebServlet(name = "EditBookServlet", urlPatterns = {"/editBook"})
public class EditBookServlete extends HttpServlet {

    @Inject
    private BookService bookService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        if (user == null || user.getMembershipType() != MembershipType.ADMIN) {
            response.sendRedirect("dashboard?error=Unauthorized");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect("dashboard?error=Missing+Book+ID");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            Book book = bookService.getBookById(id);
            request.setAttribute("book", book);
            request.getRequestDispatcher("edit-book.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("dashboard?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        if (user == null || user.getMembershipType() != MembershipType.ADMIN) {
            response.sendRedirect("dashboard?error=Unauthorized");
            return;
        }

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String category = request.getParameter("category");
            String totalCopiesStr = request.getParameter("totalCopies");
            String availableCopiesStr = request.getParameter("availableCopies");

            Book payload = new Book();
            if (title != null && !title.isBlank()) payload.setTitle(title);
            if (author != null && !author.isBlank()) payload.setAuthor(author);
            if (category != null && !category.isBlank()) payload.setCategory(category);
            if (totalCopiesStr != null && !totalCopiesStr.isBlank()) {
                payload.setTotalCopies(Integer.parseInt(totalCopiesStr));
            }
            if (availableCopiesStr != null && !availableCopiesStr.isBlank()) {
                payload.setAvailableCopies(Integer.parseInt(availableCopiesStr));
            }

            bookService.updateBookDetails(id, payload);
            response.sendRedirect("dashboard?msg=" + URLEncoder.encode("Book updated successfully", StandardCharsets.UTF_8));
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null || message.isBlank()) message = "Update failed";
            response.sendRedirect("dashboard?error=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
        }
    }
}

