package com.javainuse.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.javainuse.db.BookRepository;
import com.javainuse.exceptions.NoSuchElementFoundException;
import com.javainuse.model.Book;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "books")
public class BookController {
	
	private byte[] bytes;

	private final BookRepository bookRepo;
	
	@Autowired
	public BookController(BookRepository bookRepo) {
		this.bookRepo = bookRepo;
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getBooks(@RequestParam("page") int pages, @RequestParam("size") int size)
			throws ResourceNotFoundException {
		Pageable page = PageRequest.of(pages, size, Sort.by("name").descending());
		Page<Book> pagedResult = bookRepo.findAll(page);
		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("book", pagedResult.getContent());
		responseBody.put("count", bookRepo.count());
		if (pagedResult.hasContent()) {
			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} else {
			throw new ResourceNotFoundException("Book not found in the database");
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Book> getUserById(@PathVariable("id") Long id)

			throws ResourceNotFoundException {
		Book book = bookRepo.findById(id)
				.orElseThrow(() -> new NoSuchElementFoundException("User not found for this id :: " + id));
		return ResponseEntity.ok().body(book);
	}
	
	@PostMapping("/upload")
	public void uploadImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
		this.bytes = file.getBytes();
	}
	
	@PostMapping(path = "/add", consumes = "application/json")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Book createBook(@Valid @RequestBody Book book) {
		return bookRepo.save(book);
	}

	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<String> deleteBook(@PathVariable("id") Long id) {
		try {
			bookRepo.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}
	
	@PutMapping("/update")
	public void updateBook(@RequestBody Book book) {
		bookRepo.save(book);
	}
}