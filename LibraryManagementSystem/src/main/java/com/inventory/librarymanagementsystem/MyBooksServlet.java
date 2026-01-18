package com.inventory.librarymanagementsystem;

import com.inventory.librarymanagementsystem.Service.BorrowingRecordService; // <--- Import the correct Service
import com.inventory.librarymanagementsystem.entities.BorrowingRecord;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

    @WebServlet(name = "MyBooksServlet", urlPatterns = {"/my-books", "/returnBook"})
public class MyBooksServlet extends HttpServlet {

    @Inject
    private BorrowingRecordService borrowingRecordService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<BorrowingRecord> records = borrowingRecordService.getUserRecord(user.getId());
        request.setAttribute("borrowingRecords", records);
        request.getRequestDispatcher("my-books.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String recordIdStr = request.getParameter("recordId");

            if (recordIdStr != null && !recordIdStr.isEmpty()) {
                Long recordId = Long.parseLong(recordIdStr);

                borrowingRecordService.returnBook(recordId);

                response.sendRedirect("my-books?msg=Book+Returned+Successfully");
            } else {
                response.sendRedirect("my-books?error=Missing+Record+ID");
            }

        } catch (IllegalStateException e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect("my-books?error=" + errorMsg);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("my-books?error=System+Error");
        }
    }
}