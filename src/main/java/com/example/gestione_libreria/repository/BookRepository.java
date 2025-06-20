package com.example.gestione_libreria.repository;

import com.example.gestione_libreria.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthor(String autore);

    List<Book> findByAnnoPubblicazioneGreaterThan(Integer annoPubblicazione);

    List<Book> findByIsAvailableIsTrueAndGenre(String genere);

    List<Book> findByPriceLessThan(Double prezzo);

    Integer countByAuthor(String autore);

    List<Book> findFirst3ByGenreOrderByPriceDesc(String genere);

    List<Book> findByTitleContaining(String parola);

    Page<Book> findByGenre(String genere, Pageable pageable);

    Page<Book> findByIsAvailableIsTrue(Pageable pageable);

    @Query(value = "SELECT * FROM Book b WHERE b.anno_di_pubblicazione = ?1", nativeQuery = true)
    List<Book> findByAnnoPubblicazione(Integer annoPubblicazione);

    @Query(value = "SELECT COUNT(*) FROM Book b WHERE b.disponibile = true", nativeQuery = true)
    Integer countByAvailability();

    @Query(value = "SELECT * FROM Book b WHERE b.price > ?1 AND b.price < ?2", nativeQuery = true)
    List<Book> findByPriceBetween(Integer min, Integer max);

    @Query(value = "SELECT * FROM Book b WHERE b.price > ?1 AND b.price < ?2", nativeQuery = true)
    Page<Book> findByPriceBetweenPaginated(Integer min, Integer max, Pageable pageable);


}