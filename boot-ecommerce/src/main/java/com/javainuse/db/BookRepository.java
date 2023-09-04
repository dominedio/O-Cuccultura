package com.javainuse.db;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.javainuse.model.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
}