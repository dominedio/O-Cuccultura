package com.javainuse.db;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.javainuse.model.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>{

}
