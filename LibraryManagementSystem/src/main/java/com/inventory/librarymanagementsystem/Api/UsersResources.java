package com.inventory.librarymanagementsystem.Api;

import com.inventory.librarymanagementsystem.Service.UserService;
import com.inventory.librarymanagementsystem.Service.UserServiceImpl;
import com.inventory.librarymanagementsystem.dto.LoginRequest;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.geom.RectangularShape;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResources {
    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public Response register(User user){
        try{

            User createdUser=userService.registerUser(user);
            return Response.status(Response.Status.CREATED).entity(createdUser).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();}
    }
    @GET
    public List<User>getAllUser(){
        return userService.getAllUsers();
    }
    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest){

        User user=userService.Login(loginRequest.getEmail(),loginRequest.getPassword());
        if(user!=null){
            return Response.ok(user).build();
        }
        else{
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
        }

    }

}
