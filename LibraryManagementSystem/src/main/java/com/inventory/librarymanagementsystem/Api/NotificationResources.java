package com.inventory.librarymanagementsystem.Api;

import com.inventory.librarymanagementsystem.Service.NotificationService;
import com.inventory.librarymanagementsystem.entities.Notification;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/notification")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResources {
    @Inject
    NotificationService notificationService;

    @GET
    @Path("/{id}")
    public List<Notification>getAllUserNotification(@PathParam("id")Long id){
        return notificationService.getAllNotificationsForUser(id);
    }

}
