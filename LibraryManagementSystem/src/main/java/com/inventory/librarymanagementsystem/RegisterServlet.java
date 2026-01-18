package com.inventory.librarymanagementsystem;

import java.io.*;

import com.inventory.librarymanagementsystem.Service.UserService;
import com.inventory.librarymanagementsystem.Service.UserServiceImpl;
import com.inventory.librarymanagementsystem.entities.User;
import com.inventory.librarymanagementsystem.enums.MembershipType;
import jakarta.inject.Inject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Inject
    private UserService userService;
    public void init() {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name=request.getParameter("name");
        String email=request.getParameter("email");
        String password=request.getParameter("password");
        String roleStr = request.getParameter("role");
        MembershipType role = MembershipType.valueOf(roleStr);
        try {
            User user=new User(name,email,role,password);
            User regUser=userService.registerUser(user);
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);
            response.sendRedirect(request.getContextPath() + "/dashboard");

        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("errorMessage", "Registration failed: " + e.getMessage());

        }
    }

    public void destroy() {
    }
}