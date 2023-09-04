package com.javainuse.db;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.javainuse.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}