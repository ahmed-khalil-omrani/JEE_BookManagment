package com.inventory.librarymanagementsystem.Api;

import com.inventory.librarymanagementsystem.Service.BorrowingRecordService;
import com.inventory.librarymanagementsystem.entities.BorrowingRecord;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Past;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/borrow")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BorrowingResources {
    @Inject
    private BorrowingRecordService borrowingRecordService;

    @POST
    public Response borrowBook(@QueryParam("bookId") Long bookId,@QueryParam("userId") Long userId)
    {
        try{
            BorrowingRecord record=borrowingRecordService.borrowBook(bookId,userId);
            return Response.status(Response.Status.CREATED).entity(record).build();
        }
        catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @PUT
    @Path("/return/{id}")
    public Response returnBook(@PathParam("id") Long recordId) {
        try {
            BorrowingRecord record = borrowingRecordService.returnBook(recordId);
            return Response.ok(record).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @GET
    @Path("/active")
    public List<BorrowingRecord> getActiveBorowing() {
        return borrowingRecordService.getActiveRecord();
    }

    @GET
    @Path("/active/{id}")
    public List<BorrowingRecord> getActiveBorrowingByUser(@PathParam("id")Long id) {
        return borrowingRecordService.getUserActiveRecord(id);
    }

    @GET
    @Path("/borrow/{id}")
    public List<BorrowingRecord> getBorrowingByUser(@PathParam("id")Long id) {
        return borrowingRecordService.getUserRecord(id);
    }

}
