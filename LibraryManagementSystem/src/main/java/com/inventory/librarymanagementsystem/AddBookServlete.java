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
import java.time.LocalDate;

@WebServlet(name = "AddBookServlet", urlPatterns = {"/addBook"})
public class AddBookServlete extends HttpServlet {

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
        request.getRequestDispatcher("add-book.jsp").forward(request, response);
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
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String isbn = request.getParameter("isbn");
            String category = request.getParameter("category");
            String publishDateStr = request.getParameter("publishDate");
            String totalCopiesStr = request.getParameter("totalCopies");
            String availableCopiesStr = request.getParameter("availableCopies");
            String imageUrl = request.getParameter("imageUrl");

            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setCategory(category);
            if (publishDateStr != null && !publishDateStr.isBlank()) {
                book.setPublishDate(LocalDate.parse(publishDateStr));
            }
            Integer totalCopies = (totalCopiesStr != null && !totalCopiesStr.isBlank())
                    ? Integer.parseInt(totalCopiesStr) : null;
            Integer availableCopies = (availableCopiesStr != null && !availableCopiesStr.isBlank())
                    ? Integer.parseInt(availableCopiesStr) : null;

            if (totalCopies == null || totalCopies < 1) {
                throw new IllegalArgumentException("Total copies must be a positive number");
            }
            if (availableCopies == null) {
                availableCopies = totalCopies;
            }
            if (availableCopies < 0) {
                throw new IllegalArgumentException("Available copies must be non-negative");
            }
            if (availableCopies > totalCopies) {
                throw new IllegalArgumentException("Available copies cannot exceed total copies");
            }

            book.setTotalCopies(totalCopies);
            book.setAvailableCopies(availableCopies);
            if (imageUrl != null && !imageUrl.isBlank()) {
                book.setImageUrl(imageUrl);
            }

            bookService.addBook(book);
            response.sendRedirect("dashboard?msg=" + URLEncoder.encode("Book added successfully", StandardCharsets.UTF_8));
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null || message.isBlank()) message = "Add book failed";
            response.sendRedirect("addBook?error=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
        }
    }
}

