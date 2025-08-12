package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.dto.category.CreateCategoryDTO;
import com.accounting.accounting_tool.entity.AccountCatalogue;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.response.BasicResponse;
import jakarta.persistence.Basic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.service.CategoryService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/categories")
public class CategoryController 
{
	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> save(@Valid @RequestBody CreateCategoryDTO categoryDTO)
	{
		Category category = this.setCategoryData(categoryDTO);
		Category newCategory = this.categoryService.save(category);
		
		return new ResponseEntity<>(newCategory, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update (@Valid @RequestBody CreateCategoryDTO categoryToUpdate, @PathVariable Long id)
	{
		Category category = this.setCategoryData(categoryToUpdate, id);

		return new ResponseEntity<>(this.categoryService.update(category), HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAll()
	{
		List<Category> categoryList = this.categoryService.findAll(getAuthUsername());

		return new ResponseEntity<>(categoryList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id)
	{
		return new ResponseEntity<>(this.categoryService.findById(id, getAuthUsername()), HttpStatus.OK);
	}

	@GetMapping("/user-categories")
	public ResponseEntity<?> findByUsername()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		List<Category> categoriesOfUser = this.categoryService.findByUsername(authentication.getName());

		return new ResponseEntity<>(categoriesOfUser, HttpStatus.OK);
	}

	@GetMapping("/search/{name}")
	public ResponseEntity<?> findByName(@PathVariable String name)
	{
		return new ResponseEntity<>(this.categoryService.findByName(name, getAuthUsername()), HttpStatus.OK);
	}

	@GetMapping("/search-coincidence/{name}")
	public ResponseEntity<?> findByNameCoincidence(@PathVariable String name)
	{
		return new ResponseEntity<>(
			this.categoryService.findByNameCoincidence(name, getAuthUsername()),
			HttpStatus.OK
		);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteById (@PathVariable Long id)
	{
		String message = this.categoryService.deleteById(id, getAuthUsername());
		BasicResponse<String> response = new BasicResponse<>(message, "successful");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private Category setCategoryData(CreateCategoryDTO categoryDTO)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Category category = new Category();

		category.setName(categoryDTO.getName());
		category.setAccountCatalogue(new AccountCatalogue(categoryDTO.getAccountCatalogueId()));
		category.setUser(new User(authentication.getName()));

		if (categoryDTO.getParentCategory() != null)
			category.setParentCategory(categoryDTO.getParentCategory());

		return category;
	}

	private Category setCategoryData(CreateCategoryDTO categoryDTO, Long categoryId)
	{
		Category category = this.setCategoryData(categoryDTO);
		category.setId(categoryId);

		return category;
	}

	private Authentication getAuthentication()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private String getAuthUsername()
	{
		Authentication authentication = this.getAuthentication();

		return authentication.getName();
	}
}
