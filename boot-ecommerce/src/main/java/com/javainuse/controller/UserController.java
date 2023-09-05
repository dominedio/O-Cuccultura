package com.javainuse.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.db.UserRepository;
import com.javainuse.exceptions.NoSuchElementFoundException;
import com.javainuse.model.User;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "users")
public class UserController {

	private final UserRepository userRepo;

	@Autowired
	public UserController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping("/get")
	public ResponseEntity<Object> getUsers(@RequestParam("page") int pages, @RequestParam("size") int size)
			throws NoSuchElementFoundException {
		Pageable page = PageRequest.of(pages, size, Sort.by("name").descending());
		Page<User> pagedResult = userRepo.findAll(page);
		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("users", pagedResult.getContent());
		responseBody.put("count", userRepo.count());
		if (pagedResult.hasContent()) {
			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} else {
			throw new NoSuchElementFoundException("Users not found in the database");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id)

			throws NoSuchElementFoundException {
		User user = userRepo.findById(id)
				.orElseThrow(() -> new NoSuchElementFoundException("User not found for this id :: " + id));
		return ResponseEntity.ok().body(user);
	}

	@PostMapping(path = "/add", consumes = "application/json")
	@ResponseStatus(code = HttpStatus.CREATED)
	public User postUser(@Valid @RequestBody User user) {
		return userRepo.save(user);
	}


	@PutMapping(path = "/{id}/put", consumes = "application/json")
	public ResponseEntity<User> putUser(@Valid @PathVariable("id") Long id, @RequestBody User user) 
	  throws NoSuchElementFoundException{
			Optional<User> optionalUser = userRepo.findById(id);
			if (optionalUser.isPresent()) {
				User existingUser = optionalUser.get();
				existingUser.setName(user.getName());
				existingUser.setEmail(user.getEmail());
				existingUser.setPassword(user.getPassword());

				User userUpdated = userRepo.save(existingUser);

				return new ResponseEntity<>(userUpdated, HttpStatus.OK);
			} else {
				throw new NoSuchElementFoundException("User not found for this id :: " + id);
			}		
	}
/*
	@DeleteMapping(path = { "/{id}" })
	public User deleteUser(@PathVariable("id") long id) {
		User user = userRepository.getOne(id);
		userRepository.deleteById(id);
		return user;
	}
	*/
	
	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
		try {
			userRepo.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
	}

}