package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.Repository.BookRepository;
import com.inventory.librarymanagementsystem.Repository.UserRepository;
import com.inventory.librarymanagementsystem.entities.Book;
import com.inventory.librarymanagementsystem.entities.User;
import com.inventory.librarymanagementsystem.enums.MembershipType;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class BookServiceImpl implements BookService {
    private static final Logger LOGGER = Logger.getLogger(BookServiceImpl.class.getName());
    @Inject
    BookRepository bookRepository;
    @Inject
    UserRepository userRepository;
    @Override
    public Book addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()){
            throw new IllegalArgumentException("Book title cannot be empty");
        }if (book.getTotalCopies() < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative");
        }
        if(!bookRepository.findByTitle(book.getTitle()).isEmpty()){
            throw new IllegalArgumentException("book Already exists");
        }
        book.setAvailableCopies(book.getTotalCopies());
        return bookRepository.create(book);
    }

    @Override
    public Book getBookById(Long id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with ID: " + id);
        }
        return book;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitle(query);
    }
    public Book updateBookDetails(Long id, Book updatedData) {
        Book existingBook = getBookById(id);
        if(existingBook==null){
            throw new IllegalArgumentException("Book not found");

        }
        if (updatedData.getTitle() != null) existingBook.setTitle(updatedData.getTitle());
        if (updatedData.getAuthor() != null) existingBook.setAuthor(updatedData.getAuthor());
        if (updatedData.getCategory() != null) existingBook.setCategory(updatedData.getCategory());
        if (updatedData.getTotalCopies() != null) existingBook.setTotalCopies(updatedData.getTotalCopies());

        if (updatedData.getAvailableCopies() != null) existingBook.setAvailableCopies(updatedData.getAvailableCopies());
        return bookRepository.update(existingBook);
    }

    @Override
    public void deleteBook(Long id, Long userId) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new IllegalArgumentException("Cannot delete: Book with ID " + id + " not found.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));
        if (user.getMembershipType() != MembershipType.ADMIN) {
            throw new IllegalArgumentException("Access Denied: Only ADMINs can delete books.");
        }
        LOGGER.info("Deleting book: " + book.getTitle() + " (ID: " + id + ")");
        bookRepository.delete(book);
    }
    @Override
    public List<Book> getBooksByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return List.of();
        }
        return bookRepository.findByCategory(category);
    }
    @Override
    public List<Book> getBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return List.of();
        }
        return bookRepository.findByAuthor(author);
    }
    @Override
    public Book getBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        Book book = bookRepository.findByISBN(isbn);

        if (book == null) {
            throw new IllegalArgumentException("No book found with ISBN: " + isbn);
        }
        return book;
    }
}
