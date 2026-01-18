package com.inventory.librarymanagementsystem.config;

import com.inventory.librarymanagementsystem.Service.BookService;
import com.inventory.librarymanagementsystem.entities.Book;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.Random;

@Singleton
@Startup
public class dataBaseInitializer {
    @Inject
    private BookService bookService;
    private static final String OPEN_LIBRARY_URL = "https://openlibrary.org/search.json?q=programming&limit=20";
    @PostConstruct
    public void init(){
        try{
            if(!bookService.getAllBooks().isEmpty()){
                return;
            }
            Client client= ClientBuilder.newClient();
            String jsonResponse=client.target(OPEN_LIBRARY_URL).request(MediaType.APPLICATION_JSON).get(String.class);
            try(JsonReader jsonReader= Json.createReader(new StringReader(jsonResponse))){
                JsonObject root=jsonReader.readObject();
                JsonArray docs=root.getJsonArray("docs");
                for (int i = 0; i < docs.size(); i++) {
                    JsonObject doc = docs.getJsonObject(i);
                    try {
                        Book book = mapJsonToBook(doc);
                        bookService.addBook(book);


                }catch (Exception e){System.err.println("Skipped a book due to error: " + e.getMessage());}
                }
            }client.close();
        } catch (Exception e){System.err.println(e.getMessage());
        e.printStackTrace();
        }
    }
    private Book mapJsonToBook(JsonObject doc) {
        Book book = new Book();
        book.setTitle(doc.getString("title", "Unknown Title"));
        if (doc.containsKey("author_name") && !doc.getJsonArray("author_name").isEmpty()) {
            book.setAuthor(doc.getJsonArray("author_name").getString(0));
        } else {
            book.setAuthor("Unknown Author");
        }
        if (doc.containsKey("isbn") && !doc.getJsonArray("isbn").isEmpty()) {
            book.setIsbn(doc.getJsonArray("isbn").getString(0));
        } else {
            book.setIsbn("TEMP-" + System.nanoTime());
        }
        if (doc.containsKey("first_publish_year")) {
            int year = doc.getInt("first_publish_year");
            book.setPublishDate(LocalDate.of(year, 1, 1));
        } else {
            book.setPublishDate(LocalDate.now());
        }

        if (doc.containsKey("cover_i")) {
            int coverId = doc.getInt("cover_i");

            String url = "https://covers.openlibrary.org/b/id/" + coverId + "-L.jpg";
            book.setImageUrl(url);
        } else {
            book.setImageUrl("https://via.placeholder.com/300x450?text=No+Cover");
        }
        if (doc.containsKey("subject") && !doc.getJsonArray("subject").isEmpty()) {
            book.setCategory(doc.getJsonArray("subject").getString(0));
        } else {
            book.setCategory("General");
        }
        book.setTotalCopies(new Random().nextInt(15) + 5);
        book.setAvailableCopies(book.getTotalCopies());
        return book;
    }


}
