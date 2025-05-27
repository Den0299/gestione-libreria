package com.example.gestione_libreria.entity;

import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    @Column(name = "anno_di_pubblicazione")
    private Integer annoPubblicazione;

    private String genre;

    @Column(name = "disponibile")
    private boolean isAvailable;
    private Double price;

    public Book() {
    }

    public Book(Long id, String title, String author, Integer annoPubblicazione, String genre, boolean isAvailable, Double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.annoPubblicazione = annoPubblicazione;
        this.genre = genre;
        this.isAvailable = isAvailable;
        this.price = price;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(Integer annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
