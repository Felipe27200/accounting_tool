package com.accounting.accounting_tool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.accounting_tool.entity.AccountCatalogue;
import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.repository.CategoryRepository;
import com.accounting.accounting_tool.service.AccountCatalogueService;
import com.accounting.accounting_tool.service.CategoryService;
import com.accounting.accounting_tool.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "${apiPrefix}/categories")
public class CategoryController 
{
	private final CategoryService categoryService;
	private final AccountCatalogueService accountCatalogueService;
	private final UserService userService;
	
	@Autowired
	public CategoryController(
		CategoryService categoryService,
		AccountCatalogueService accountCatalogueService,
		UserService userService
	) {
		this.categoryService = categoryService;
		this.accountCatalogueService = accountCatalogueService;
		this.userService = userService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> save(@Valid @RequestBody Category category, Long accountCatalogueId)
	{
		AccountCatalogue accountCatalogue = this.accountCatalogueService.findById(accountCatalogueId);
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findByUsername(authentication.getName());

		Category newCategory = this.categoryService.save(category, user.getUsername(), accountCatalogue.getId());
		
		return new ResponseEntity<>(newCategory, HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAll()
	{
		return new ResponseEntity<>(this.categoryService.findAll(), HttpStatus.OK);
	}
}
