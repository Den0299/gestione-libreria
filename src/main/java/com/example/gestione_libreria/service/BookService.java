package com.example.gestione_libreria.service;

import com.example.gestione_libreria.entity.Book;
import com.example.gestione_libreria.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> selectAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> deleteBook(Book book) {
        bookRepository.delete(book);
        return Optional.of(book);
    }

    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }

    public Optional<Book> updateBook(Long id, Book bookDetails) {

        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            optionalBook.get().setTitle(bookDetails.getTitle());
            optionalBook.get().setAuthor(bookDetails.getAuthor());
            optionalBook.get().setAnnoPubblicazione(bookDetails.getAnnoPubblicazione());
            optionalBook.get().setGenre(bookDetails.getGenre());
            optionalBook.get().setAvailable(bookDetails.isAvailable());
            optionalBook.get().setPrice(bookDetails.getPrice());

            Book book = bookRepository.save(optionalBook.get());
            return Optional.of(book);
        }

        return Optional.empty();
    }

    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> findByAnnoPubblicazioneGreaterThan(Integer annoPubblicazione) {
        return bookRepository.findByAnnoPubblicazioneGreaterThan(annoPubblicazione);
    }

    public List<Book> findByIsAvailableIsTrueAndGenre(String genre) {
        return bookRepository.findByIsAvailableIsTrueAndGenre(genre);
    }

    public List<Book> findByPriceLessThan(Double price) {
        return bookRepository.findByPriceLessThan(price);
    }

    public Integer countByAuthor(String author) {
        return bookRepository.countByAuthor(author);
    }

    public List<Book> findFirst3ByGenreOrderByPriceDesc(String genre) {
        return bookRepository.findFirst3ByGenreOrderByPriceDesc(genre);
    }

    public List<Book> findByTitleContaining(String word) {
        return bookRepository.findByTitleContaining(word);
    }

    public Page<Book> findByGenrePaginated(String genre, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("price").descending());

        return bookRepository.findByGenre(genre, pageable);
    }

    public Page<Book> findByIsAvailableIsTruePaginated(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("annoPubblicazione"));

        return bookRepository.findByIsAvailableIsTrue(pageable);
    }

    public List<Book> findByAnnoPubblicazione(Integer annoPubblicazione) {
        return bookRepository.findByAnnoPubblicazione(annoPubblicazione);
    }

    public Integer countByAvailability() {
        return bookRepository.countByAvailability();
    }

    public List<Book> findByPriceBetween(Integer min, Integer max) {
        return bookRepository.findByPriceBetween(min, max);

    }

    public Page<Book> findByPriceBetweenPaginated(Integer min, Integer max, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        return bookRepository.findByPriceBetweenPaginated(min, max, pageable);
    }
}
