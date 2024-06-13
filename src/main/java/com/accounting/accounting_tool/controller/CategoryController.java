package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.dto.category.CreateCategoryDTO;
import com.accounting.accounting_tool.entity.AccountCatalogue;
import com.accounting.accounting_tool.entity.User;
import org.modelmapper.ModelMapper;
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

import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.service.AccountCatalogueService;
import com.accounting.accounting_tool.service.CategoryService;
import com.accounting.accounting_tool.service.UserService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/categories")
public class CategoryController 
{
	private final CategoryService categoryService;
	private final AccountCatalogueService accountCatalogueService;
	private final UserService userService;
	private final ModelMapper modelMapper;

	@Autowired
	public CategoryController(
		CategoryService categoryService,
		AccountCatalogueService accountCatalogueService,
		UserService userService,
		ModelMapper modelMapper
	) {
		this.categoryService = categoryService;
		this.accountCatalogueService = accountCatalogueService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> save(@Valid @RequestBody CreateCategoryDTO categoryDTO)
	{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Category category = new Category();

		category.setName(categoryDTO.getName());
		category.setAccountCatalogue(new AccountCatalogue(categoryDTO.getAccountCatalogueId()));
		category.setUser(new User(authentication.getName()));

		if (categoryDTO.getParentCategory() != null)
			category.setParentCategory(categoryDTO.getParentCategory());

		Category newCategory = this.categoryService.save(category, authentication.getName(), categoryDTO.getAccountCatalogueId());
		
		return new ResponseEntity<>(newCategory, HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAll()
	{
		List<Category> categoryList = this.categoryService.findAll();

		return new ResponseEntity<>(categoryList, HttpStatus.OK);
	}
}
