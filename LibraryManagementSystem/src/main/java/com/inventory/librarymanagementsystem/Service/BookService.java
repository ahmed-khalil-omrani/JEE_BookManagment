package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.Repository.BookRepository;
import com.inventory.librarymanagementsystem.entities.Book;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Local
public interface BookService {

    Book addBook(Book book);
    Book getBookById(Long id);
    List<Book> getAllBooks();
    Book updateBookDetails(Long id, Book updatedData);
    void deleteBook(Long id,Long userId);
    List<Book> searchBooks(String query);
    List<Book> getBooksByCategory(String category);
    List<Book> getBooksByAuthor(String author);
    Book getBookByIsbn(String isbn);



}
