package com.example.gestione_libreria;

import com.example.gestione_libreria.controller.BookController;
import com.example.gestione_libreria.entity.Book;
import com.example.gestione_libreria.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;



@SpringBootTest
@AutoConfigureMockMvc
class BookTests {

	@MockitoBean
	private BookService bookService;

	@Autowired
	private BookController bookController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Book book1;
	private Book book2;
	private Integer page;
	private Integer size;
	private Page<Book> booksPage;

	@BeforeEach
	public void setUp() throws Exception {
		book1 = new Book(
				1L,
				"Il nome della rosa",
				"Umberto Eco",
				1980,
				"Fantasy",
				true,
				15.0
		);

		book2 = new Book(
				2L,
				"L'ombra del vento",
				"Carlos Ruiz Zafón",
				2001,
				"Fantasy",
				true,
				21.0
		);

		page = 0;
		size = 2;

		booksPage = new PageImpl<>(List.of(book1, book2));

	}

	@Test
	void contextLoads() {
		assertThat(bookController).isNotNull();
	}

	@Test
	public void testCreateBook() throws Exception {

		/*
		Usa Mockito per simulare (mock) il comportamento del book1Service.
		Quando viene chiamato createBook(...) su book1Service con qualunque oggetto Book,
		allora restituisci l’oggetto book1
		 */
		when(bookService.createBook(any(Book.class))).thenReturn(book1);

		mockMvc.perform(post("/api/books/create-book")
						.contentType(MediaType.APPLICATION_JSON) //Specifica che il tipo di contenuto della richiesta è JSON.
						.content(objectMapper.writeValueAsString(book1))) // Converte l’oggetto book1 in stringa JSON con objectMapper e lo inserisce nel corpo della richiesta.
				.andExpect(status().isOk()) // Aspettati che la risposta HTTP abbia status 200 OK.
				.andExpect(jsonPath("$.title").value(book1.getTitle())) // Usa JsonPath per verificare che nel JSON di risposta ci sia un campo title con il valore uguale a book1.getTitle().
				.andDo(print()); // Stampa nel log l’intera richiesta e risposta
	}

	@Test
	public void testGetAllBooks() throws Exception {
		when(bookService.selectAllBooks()).thenReturn(List.of(book1));

		mockMvc.perform(get("/api/books/select-all-books")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testUpdateBook() throws Exception{
		when(bookService.updateBook(eq(book1.getId()), any(Book.class))).thenReturn(Optional.of(book1));

		mockMvc.perform(put("/api/books/update-book/{id}", book1.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(book1)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(book1.getTitle()))
				.andDo(print());
	}

	@Test
	public void testUpdateBookNotFound() throws Exception{
		when(bookService.updateBook(eq(book1.getId()), any(Book.class))).thenReturn(Optional.empty());

		mockMvc.perform(put("/api/books/update-book/{id}", book1.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(book1)))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testDeleteBook() throws Exception {
		// Simula che il servizio non trovi il libro da cancellare
		when(bookService.deleteBook(any(Book.class))).thenReturn(Optional.of(book1));

		mockMvc.perform(delete("/api/books/delete-book")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(book1)))
				.andExpect(status().isOk())
				.andDo(print());

		verify(bookService, times(1)).deleteBook(any(Book.class));

	}

	@Test
	public void testDeleteBookNotFound() throws Exception {
		// Simula che il servizio non trovi il libro da cancellare
		when(bookService.deleteBook(any(Book.class))).thenReturn(Optional.empty());

		mockMvc.perform(delete("/api/books/delete-book")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(book1)))
				.andExpect(status().isNoContent())
				.andDo(print());

		verify(bookService, times(1)).deleteBook(any(Book.class));

	}

	@Test
	public void testFindById() throws Exception {
		when(bookService.findById(book1.getId())).thenReturn(Optional.of(book1));

		mockMvc.perform(get("/api/books/find-by-id/{id}", book1.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testFindByIdNotFound() throws Exception {
		when(bookService.findById(book1.getId())).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/books/find-by-id/{id}", book1.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testDeleteAll() throws Exception {
		//per i metodi void questo è uno dei metodi utilizzabili:
		doNothing().when(bookService).deleteAllBooks();

		mockMvc.perform(delete("/api/books/delete-all-books")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andDo(print());

		/* per verificare che abbia cancellato possiamo usare il verify:
		nel caso il service non venga invocato oppure il metodo non faccia quello per cui è stato sviluppato
		fallisce il test. (times 1 si riferisce a quante volte il metodo viene "invocato") */
		verify(bookService, times(1)).deleteAllBooks();
	}

	@Test
	public void findByAuthor() throws Exception {
		when(bookService.findByAuthor(book1.getAuthor())).thenReturn((List.of(book1)));

		mockMvc.perform(get("/api/books/select-by-author")
						.param("author", book1.getAuthor())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].author").value(book1.getAuthor()))
				.andDo(print());

	}

	@Test
	public void findByAuthorNotFound() throws Exception {
		when(bookService.findByAuthor(book1.getAuthor())).thenReturn((Collections.emptyList()));

		mockMvc.perform(get("/api/books/select-by-author")
						.param("author", book1.getAuthor())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void findByAnnoPubblicazioneGreaterThan() throws Exception {
		when(bookService.findByAnnoPubblicazioneGreaterThan(2000)).thenReturn((List.of(book2)));

		mockMvc.perform(get("/api/books/select-by-annoPubblicazione-greater-than")
						.param("annoPubblicazione", "2000")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].annoPubblicazione").value(2001))
				.andExpect(jsonPath("$[0].title").value("L'ombra del vento"))
				.andDo(print());
	}

	@Test
	public void findByAnnoPubblicazioneGreaterThanNotFound() throws Exception {
		when(bookService.findByAnnoPubblicazioneGreaterThan(1990)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/books/select-by-annoPubblicazione-greater-than")
						.param("annoPubblicazione", "1990")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void findByIsAvailableIsTrueAndGenre() throws Exception {
		when(bookService.findByIsAvailableIsTrueAndGenre(book1.getGenre())).thenReturn((List.of(book1)));

		mockMvc.perform(get("/api/books/select-by-isAvailable-isTrue-and-by-genre")
						.param("genre", book1.getGenre())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].genre").value(book1.getGenre()))
				.andExpect(jsonPath("$[0].available").value(true))
				.andDo(print());

	}

	@Test
	public void findByIsAvailableIsTrueAndGenreNotFound() throws Exception {
		when(bookService.findByIsAvailableIsTrueAndGenre(book1.getGenre())).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/books/select-by-isAvailable-isTrue-and-by-genre")
						.param("genre",book1.getGenre())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());

	}

	@Test
	public void findByPriceLessThan() throws Exception {
		when(bookService.findByPriceLessThan(20.0)).thenReturn((List.of(book1)));

		mockMvc.perform(get("/api/books/select-by-price-less-than")
						.param("price", "20.0")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].price").value(book1.getPrice()))
				.andDo(print());

	}

	@Test
	public void findByPriceLessThanNotFound() throws Exception {
		when(bookService.findByPriceLessThan(book1.getPrice())).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/books/select-by-price-less-than")
						.param("price", String.valueOf(book1.getPrice()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());

	}

	@Test
	public void countByAuthor() throws Exception {
		when(bookService.countByAuthor(book1.getAuthor())).thenReturn(1);

		mockMvc.perform(get("/api/books/count-by-author")
						.param("author", book1.getAuthor())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("1"))
				.andDo(print());
	}

	@Test
	public void countByAuthorNotFound() throws Exception {
		when(bookService.countByAuthor(book1.getAuthor())).thenReturn(0);

		mockMvc.perform(get("/api/books/count-by-author")
						.param("author", book1.getAuthor())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andDo(print());
	}

	@Test
	public void findFirst3ByGenreOrderByPriceDesc() throws Exception {
		when(bookService.findFirst3ByGenreOrderByPriceDesc("Fantasy")).thenReturn((List.of(book1, book2)));

		mockMvc.perform(get("/api/books/select-first-3-books-by-genre-order-by-price-desc")
						.param("genre", "Fantasy")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].price").value(book1.getPrice()))
				.andExpect(jsonPath("$[1].price").value(book2.getPrice()))
				.andDo(print());
	}

	@Test
	public void findFirst3ByGenreOrderByPriceDescNotFound() throws Exception {
		when(bookService.findFirst3ByGenreOrderByPriceDesc("Fantasy")).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/books/select-first-3-books-by-genre-order-by-price-desc")
						.param("genre", "Fantasy")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void findByTitleContaining() throws Exception {
		when(bookService.findByTitleContaining("nome")).thenReturn((List.of(book1)));

		mockMvc.perform(get("/api/books/select-by-title-containing")
						.param("word", "nome")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value(containsString("nome")))
				.andDo(print());

	}

	@Test
	public void findByTitleContainingNotFound() throws Exception {
		when(bookService.findByTitleContaining(book1.getTitle())).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/books/select-by-title-containing")
						.param("word", "nome")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void findByGenrePaginated() throws Exception {
		when(bookService.findByGenrePaginated("Fantasy", page, size)).thenReturn(booksPage);

		mockMvc.perform(get("/api/books/select-by-genre-paginated")
						.param("genre", "Fantasy")
						.param("page", String.valueOf(page))
						.param("size", String.valueOf(size))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2))           // La lista di libri nella pagina ha 2 elementi
				.andExpect(jsonPath("$.content[0].genre").value("Fantasy"))   // Primo libro ha il genere corretto
				.andExpect(jsonPath("$.content[1].genre").value("Fantasy"))   // Secondo libro idem
				.andExpect(jsonPath("$.totalElements").value(2))              // Totale elementi
				.andExpect(jsonPath("$.number").value(page))                               // Numero pagina
				.andDo(print());
	}

	@Test
	public void findByAvailabilityPaginated() throws Exception {
		when(bookService.findByIsAvailableIsTruePaginated(page, size)).thenReturn(booksPage);

		mockMvc.perform(get("/api/books/select-by-availability-paginated")
						.param("page", String.valueOf(page))
						.param("size", String.valueOf(size))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2))
				.andExpect(jsonPath("$.content[0].available").value(true))
				.andExpect(jsonPath("$.content[1].available").value(true))
				.andExpect(jsonPath("$.totalElements").value(2))
				.andExpect(jsonPath("$.number").value(page))
				.andDo(print());
	}
	@Test
	public void findByAnnoPubblicazione() throws Exception {
		when(bookService.findByAnnoPubblicazione(2001)).thenReturn((List.of(book2)));

		mockMvc.perform(get("/api/books/select-by-annoPubblicazione")
						.param("annoPubblicazione", "2001")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].annoPubblicazione").value(2001))
				.andDo(print());
	}

	@Test
	public void findByAnnoPubblicazioneThanNotFound() throws Exception {
		when(bookService.findByAnnoPubblicazione(1990)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/books/select-by-annoPubblicazione-greater-than")
						.param("annoPubblicazione", "1990")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void countByAvailability() throws Exception {
		when(bookService.countByAvailability()).thenReturn(1);

		mockMvc.perform(get("/api/books/count-by-availability")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("1"))
				.andDo(print());
	}

	@Test
	public void countByAvailabilityNotFound() throws Exception {
		when(bookService.countByAvailability()).thenReturn(0);

		mockMvc.perform(get("/api/books/count-by-availability")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}
	@Test
	public void findByPriceBetween() throws Exception {
		when(bookService.findByPriceBetween(10, 20)).thenReturn((List.of(book1)));

		mockMvc.perform(get("/api/books/select-by-price-between")
						.param("min", "10")
						.param("max", "20")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].price").value(15.0))
				.andDo(print());
	}

	@Test
	public void findByPriceBetweenNotFound() throws Exception {
		when(bookService.findByPriceBetween(10, 20)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/books/select-by-price-between")
						.param("min", "10")
						.param("max", "20")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void findByPriceBetweenPaginated() throws Exception {
		when(bookService.findByPriceBetweenPaginated(10, 20, page, size)).thenReturn(booksPage);

		mockMvc.perform(get("/api/books/select-by-price-between-paginated")
						.param("min", "10")
						.param("max", "20")
						.param("page", String.valueOf(page))
						.param("size", String.valueOf(size))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2))
				.andExpect(jsonPath("$.content[0].price").value(15.0))
				.andExpect(jsonPath("$.content[1].price").value(21.0))
				.andExpect(jsonPath("$.totalElements").value(2))
				.andExpect(jsonPath("$.number").value(page))
				.andDo(print());
	}






}
