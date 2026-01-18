package com.inventory.librarymanagementsystem;

import com.inventory.librarymanagementsystem.Service.BookService;
import com.inventory.librarymanagementsystem.Service.BookServiceImpl;
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
import java.util.logging.Logger;

@WebServlet(name = "DeleteBookServlet", urlPatterns = {"/deleteBook"})
public class DeleteBookServlete extends HttpServlet {
    @Inject
    private BookService bookService;
    private static final Logger LOGGER = Logger.getLogger(DeleteBookServlete.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        if (user == null || user.getMembershipType() != MembershipType.ADMIN) {
            response.sendRedirect("dashboard?error=Unauthorized+Action:+Admins+Only");
            return;
        }
        try {
            String idStr = request.getParameter("id");

            if (idStr != null && !idStr.isEmpty()) {
                Long bookId = Long.parseLong(idStr);
                bookService.deleteBook(bookId, user.getId());

                response.sendRedirect("dashboard?msg=Book+Deleted+Successfully");
            } else {
                response.sendRedirect("dashboard?error=Missing+Book+ID");
            }

        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message == null) message = "Invalid Request";
            response.sendRedirect("dashboard?error=" + message);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard?error=System+Error:+Could+not+delete+book");
        }
    }
}
