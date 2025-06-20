package com.example.gestione_libreria.controller;

import com.example.gestione_libreria.entity.Book;
import com.example.gestione_libreria.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/select-all-books")
    public ResponseEntity<List<Book>> selectAllBooks() {

        List<Book> books = bookService.selectAllBooks();

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Optional<Book>> selectAllBooks(@PathVariable Long id) {

       Optional<Book> book = bookService.findById(id);

       if (book.isPresent()) {
           return ResponseEntity.ok().body(book);
       }

       return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-book")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {

        Book newBook = bookService.createBook(book);

        return ResponseEntity.ok().body(newBook);
    }

    @DeleteMapping("/delete-book")
    public ResponseEntity<Optional<Book>> deleteBook(@RequestBody Book book) {

        Optional<Book> optionalBook = bookService.deleteBook(book);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(optionalBook);
    }

    @DeleteMapping("/delete-all-books")
    public ResponseEntity<Void> deleteAllBooks() {
        bookService.deleteAllBooks();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-book/{id}")
    public ResponseEntity<Optional<Book>> updateBook(@PathVariable Long id,
                                                     @RequestBody Book bookDetails) {

        Optional<Book> optionalBook = bookService.updateBook(id, bookDetails);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(optionalBook);
    }

    @GetMapping("/select-by-author")
    public ResponseEntity<List<Book>> selectByAuthor(@RequestParam String author) {

        List<Book> books = bookService.findByAuthor(author);

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/select-by-annoPubblicazione-greater-than")
    public ResponseEntity<List<Book>> selectByAnnoPubblicazioneGreaterThan(@RequestParam Integer annoPubblicazione) {

        List<Book> books = bookService.findByAnnoPubblicazioneGreaterThan(annoPubblicazione);

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/select-by-isAvailable-isTrue-and-by-genre")
    public ResponseEntity<List<Book>> selectByIsAvailableIsTrueAndByGenre(@RequestParam String genre) {

        List<Book> books = bookService.findByIsAvailableIsTrueAndGenre(genre);

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/select-by-price-less-than")
    public ResponseEntity<List<Book>> selectByPriceLessThan(@RequestParam Double price) {

        List<Book> books = bookService.findByPriceLessThan(price);

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/count-by-author")
    public ResponseEntity<Integer> countBooksByAuthor(@RequestParam String author) {

        Integer numberOfBooks = bookService.countByAuthor(author);

        if (numberOfBooks == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(numberOfBooks);
    }

    @GetMapping("/select-first-3-books-by-genre-order-by-price-desc")
    public ResponseEntity<List<Book>> findFirst3ByPriceOrderByPriceDesc(@RequestParam String genre) {

        List<Book> books = bookService.findFirst3ByGenreOrderByPriceDesc(genre);

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/select-by-title-containing")
    public ResponseEntity<List<Book>> findByTitleContaining(@RequestParam String word) {

        List<Book> books = bookService.findByTitleContaining(word);

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/select-by-genre-paginated")
    public ResponseEntity<Page<Book>> findByGenrePaginated(@RequestParam String genre,
                                                           @RequestParam Integer page,
                                                           @RequestParam Integer size) {

        Page<Book> paginatedBooks = bookService.findByGenrePaginated(genre, page, size);

        return ResponseEntity.ok().body(paginatedBooks);
    }

    @GetMapping("/select-by-availability-paginated")
    public ResponseEntity<Page<Book>> findByAvailabilityPaginated(@RequestParam Integer page,
                                                                  @RequestParam Integer size) {

        Page<Book> paginatedBooks = bookService.findByIsAvailableIsTruePaginated(page, size);

        return ResponseEntity.ok().body(paginatedBooks);
    }

    @GetMapping("/select-by-annoPubblicazione")
    public ResponseEntity<List<Book>> findByAnnoPubblicazione(@RequestParam Integer annoPubblicazione) {

        List<Book> books = bookService.findByAnnoPubblicazione(annoPubblicazione);

        return ResponseEntity.ok().body(books);

    }

    @GetMapping("/count-by-availability")
    public ResponseEntity<Integer> countByAvailability() {

        Integer numberOfBooks = bookService.countByAvailability();

        return ResponseEntity.ok().body(numberOfBooks);
    }

    @GetMapping("/select-by-price-between")
    public ResponseEntity<List<Book>> findByPriceBetween(@RequestParam Integer min,
                                                         @RequestParam Integer max) {

        List<Book> books = bookService.findByPriceBetween(min, max);

        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/select-by-price-between-paginated")
    public ResponseEntity<Page<Book>> findByPriceBetween(@RequestParam Integer min,
                                                         @RequestParam Integer max,
                                                         @RequestParam Integer page,
                                                         @RequestParam Integer size) {

        Page<Book> paginatedBooks = bookService.findByPriceBetweenPaginated(min, max, page, size);

        return ResponseEntity.ok().body(paginatedBooks);
    }

}
