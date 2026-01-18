package com.inventory.librarymanagementsystem.Api;

import com.inventory.librarymanagementsystem.Service.BookService;
import com.inventory.librarymanagementsystem.entities.Book;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResources {
    @Inject
    private BookService bookService;

    @GET
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id")Long id){
        try{Book book=bookService.getBookById(id);
            return Response.ok(book).build();
    }catch (IllegalArgumentException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response addBook(Book book) {
        try {
            Book createdBook = bookService.addBook(book);
            return Response.status(Response.Status.CREATED).entity(createdBook).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage()).build();
        }
    }
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book book) {
        try {
            Book updatedBook = bookService.updateBookDetails(id, book);
            return Response.ok(updatedBook).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{userId}/{id}")
    public Response deleteBook(@PathParam("userId") Long userId,@PathParam("id") Long id){
        try {
            bookService.deleteBook(id,userId);
            return Response.noContent().build();

        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @GET
    @Path("/search")
    public List<Book> searchBooks(@QueryParam("q") String query) {
        return bookService.searchBooks(query);
    }
    @GET
    @Path("/category/{category}")
    public List<Book> getByCategory(@PathParam("category") String category) {
        return bookService.getBooksByCategory(category);
    }
    @GET
    @Path("/author/{author}")
    public List<Book> getByAuthor(@PathParam("author") String author) {
        return bookService.getBooksByAuthor(author);
    }
    @GET
    @Path("/isbn/{isbn}")
    public Response getByIsbn(@PathParam("isbn") String isbn) {
        try {
            Book book = bookService.getBookByIsbn(isbn);
            return Response.ok(book).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage()).build();
        }
    }
}
