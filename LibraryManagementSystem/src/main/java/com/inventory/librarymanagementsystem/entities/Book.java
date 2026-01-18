package com.inventory.librarymanagementsystem.entities;

import jakarta.persistence.*;
import jdk.jfr.TransitionTo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name="Books")
@NamedQueries({@NamedQuery(name="Book.findAll",query = "SELECT b FROM Book b ORDER BY b.title "),
        @NamedQuery(name="Book.findByTitle",query = "SELECT b FROM Book b WHERE b.title=:bookTitle ORDER BY b.title "),
        @NamedQuery(name="Book.findByAuthor",query = "SELECT b FROM Book b WHERE b.author=:bookAuthor ORDER BY b.title "),
        @NamedQuery(name="Book.findByISBN",query = "SELECT b FROM Book b WHERE b.isbn=:bookIsbn ORDER BY b.title "),
        @NamedQuery(name="Book.findByCategory",query = "SELECT b FROM Book b WHERE b.category=:bookCategory ORDER BY b.title "),


})
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="title",length = 100)
    private String title;


    @Column(name="author",length = 100)
    private String author;

    @Column(name="isbn",unique = true)
    private String isbn;
    @Column(name="publishedDate")
    private LocalDate publishDate;
    @Column(name="totalCopies",nullable = false)
    private Integer totalCopies;
    @Column(name="availableCopies")
    private Integer availableCopies;
    @Column(name="category")
    private String category;

    @Column(name = "ImageUrl")
    private String ImageUrl;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Book() {
    }

    public Book(String title, String author, String isbn, LocalDate publishDate, int totalCopies, String category) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publishDate=" + publishDate +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                ", category='" + category + '\'' +
                '}';
    }
}
