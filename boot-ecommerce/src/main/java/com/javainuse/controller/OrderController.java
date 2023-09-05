package com.javainuse.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.db.OrderRepository;
import com.javainuse.exceptions.NoSuchElementFoundException;
import com.javainuse.model.Order;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "order")
public class OrderController {
	
	private final OrderRepository orderRepo;

	@Autowired
	public OrderController(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	
	@GetMapping("/get")
	public ResponseEntity<Object> getOrders(@RequestParam("page") int pages, @RequestParam("size") int size)
			throws NoSuchElementFoundException {
		Pageable page = PageRequest.of(pages, size, Sort.by("name").descending());
		Page<Order> pagedResult = orderRepo.findAll(page);
		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("order", pagedResult.getContent());
		responseBody.put("count", orderRepo.count());
		if (pagedResult.hasContent()) {
			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} else {
			throw new NoSuchElementFoundException("Order not found in the database");
		}
	}
	
	/*
	@GetMapping("/getdelete")
	public ResponseEntity<Object> getDeleteOrders()
			throws ResourceNotFoundException {
		
	}
	*/
		
	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id)

			throws NoSuchElementFoundException {
		Order order = orderRepo.findById(id)
				.orElseThrow(() -> new NoSuchElementFoundException("Order not found for this id :: " + id));
		return ResponseEntity.ok().body(order);
	}

	@PostMapping(path = "/add", consumes = "application/json")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Order postOrder(@Valid @RequestBody Order order) {
		return orderRepo.save(order);
	}

/*
	@PutMapping(path = "/{id}/put", consumes = "application/json")
	public ResponseEntity<Order> putOrder(@Valid @PathVariable("id") Long id, @RequestBody User user) 
	  throws ResourceNotFoundException{
			Optional<Order> optionalOrder = orderRepo.findById(id);
			if (optionalOrder.isPresent()) {
				Order existingOrder = optionalOrder.get();
				existingOrder.set();
				existingOrder.setEmail(order.getEmail());
				existingOrder.setPassword(order.getPassword());

				Order orderUpdated = orderRepo.save(existingOrder);

				return new ResponseEntity<>(orderUpdated, HttpStatus.OK);
			} else {
				throw new ResourceNotFoundException("Order not found for this id :: " + id);
			}		
	}
	*/
	
	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) {
		try {
			orderRepo.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}

}
