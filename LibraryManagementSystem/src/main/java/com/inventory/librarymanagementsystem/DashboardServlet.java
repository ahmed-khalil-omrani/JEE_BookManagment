package com.inventory.librarymanagementsystem;

import com.inventory.librarymanagementsystem.Service.BookService;
import com.inventory.librarymanagementsystem.entities.Book;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Inject
    private BookService bookService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String searchQuery = request.getParameter("q");
        List<Book> books = bookService.searchBooks(searchQuery);
        request.setAttribute("bookList", books);
        request.setAttribute("searchQuery", searchQuery);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}