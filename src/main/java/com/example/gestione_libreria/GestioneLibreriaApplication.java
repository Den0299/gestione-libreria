package com.example.gestione_libreria;

import com.example.gestione_libreria.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class GestioneLibreriaApplication /*implements ApplicationRunner*/ {

	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(GestioneLibreriaApplication.class, args);
	}

	/*public void initDB() {

		long startTime = System.currentTimeMillis();
		int count = 50000;

		List<Book> books = new ArrayList<>();
		for(int i=0; i < count; i++) {

			Book book1 = new Book(null, "Harry Potter e la pietra filosofale", "J.K. Rowling", 1997, "Fantasy", false, 19.99 + i);
			Book book2 = new Book(null, "Il nome della rosa", "Umberto Eco", 1980 - i, "Giallo Storico", true, 24.99);

			books.add(book1);
			books.add(book2);
		}

		bookRepository.saveAll(books);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		System.out.println(duration);


	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		initDB();

	}*/
}
