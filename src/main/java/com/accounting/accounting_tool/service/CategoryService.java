package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.AccountCatalogue;
import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.DuplicateRecordException;
import com.accounting.accounting_tool.error_handling.exception.NotFoundException;
import com.accounting.accounting_tool.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final AccountCatalogueService accountCatalogueService;

    @Autowired
    public CategoryService(
        CategoryRepository categoryRepository,
        UserService userService,
        AccountCatalogueService accountCatalogueService
    ) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.accountCatalogueService = accountCatalogueService;
    }

    public Category save (Category newCategory, String username, Long accountCatalogueId)
    {
        User user = this.userService.findByUsername(username);
        AccountCatalogue accountCatalogue = this.accountCatalogueService.findById(accountCatalogueId);
        Category checkCategory = this.categoryRepository.findByName(newCategory.getName());

        if (checkCategory != null && checkCategory.getUser().getId().equals(user.getId()))
            throw new DuplicateRecordException("You have already created the category: '" + newCategory.getName() + "'.");

        newCategory.setUser(user);
        newCategory.setAccountCatalogue(accountCatalogue);

        return this.categoryRepository.save(newCategory);
    }

    public List<Category> findAll()
    {
        List<Category> categoryList = this.categoryRepository.findAll();

        return categoryList;
    }

    public Category findByName(String name)
    {
        Category category = this.categoryRepository.findByName(name);

        if (category == null)
            throw new NotFoundException("The category: '" + name + "' does not exists");

        return category;
    }
}
