package com.inventory.librarymanagementsystem.Repository;

import com.inventory.librarymanagementsystem.entities.Book;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class BookRepository {

    @PersistenceContext(unitName="LibraryPU")
    private EntityManager em;

    public Book create(Book book){
        em.persist(book);
        return book;
    }
    public Book findById(Long id){return em.find(Book.class,id);}
    public Book update(Book book){
        return em.merge(book);
    }
    public void delete(Book book){
        if(!em.contains(book)){
        book=em.merge(book);
    }
    em.remove(book);
    }

    public List<Book> findByTitle(String title) {
        return em.createNamedQuery("Book.findByTitle", Book.class)
                .setParameter("bookTitle", title).getResultList();
    }

    public Book findByISBN(String isbn) {
        List<Book> list = em.createNamedQuery("Book.findByISBN", Book.class)
                .setParameter("bookIsbn", isbn)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    public List<Book> findAll() {
        return em.createNamedQuery("Book.findAll", Book.class).getResultList();
    }
    public List<Book> findByCategory(String category) {
        return em.createNamedQuery("Book.findByCategory", Book.class)
                .setParameter("bookCategory", category)
                .getResultList();
    }
    public List<Book> findByAuthor(String author) {
        return em.createNamedQuery("Book.findByAuthor", Book.class)
                .setParameter("bookAuthor", author)
                .getResultList();
    }

}
