package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.AccountCatalogue;
import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.DuplicateRecordException;
import com.accounting.accounting_tool.error_handling.exception.GeneralException;
import com.accounting.accounting_tool.error_handling.exception.NotFoundException;
import com.accounting.accounting_tool.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
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

    @Transactional
    public Category save (Category newCategory)
    {
        User user = this.userService.findByUsername(newCategory.getUser().getUsername());
        AccountCatalogue accountCatalogue = this.accountCatalogueService.findById(newCategory.getAccountCatalogue().getId());
        Category checkCategory = this.categoryRepository.findByName(newCategory.getName(), user.getId());

        if (checkCategory != null && compareValues(checkCategory.getUser().getId(), user.getId()))
            throw new DuplicateRecordException("You have already created the category: '" + newCategory.getName() + "'.");

        this.checkParentCategoryById(newCategory, user.getUsername(), accountCatalogue);

        newCategory.setUser(user);
        newCategory.setAccountCatalogue(accountCatalogue);

        return this.categoryRepository.save(newCategory);
    }

    @Transactional
    public Category update (Category categoryToUpdate)
    {
        User authenticatedUser = this.userService.findByUsername(categoryToUpdate.getUser().getUsername());
        Category oldCategory = this.findById(categoryToUpdate.getId(), authenticatedUser.getUsername());
        AccountCatalogue accountCatalogue = this.accountCatalogueService.findById(categoryToUpdate.getAccountCatalogue().getId());

        if (!compareValues(oldCategory.getUser().getId(), authenticatedUser.getId()))
            throw new GeneralException("The category does not belong to the user");

        this.checkParentCategoryById(categoryToUpdate, authenticatedUser.getUsername(), accountCatalogue);
        this.isCategoryAlreadyCreated(categoryToUpdate.getName(), categoryToUpdate, authenticatedUser.getId());

        categoryToUpdate.setUser(authenticatedUser);
        categoryToUpdate.setAccountCatalogue(accountCatalogue);

        return this.categoryRepository.save(categoryToUpdate);
    }

    public List<Category> findAll(String username)
    {
        User user = this.userService.findByUsername(username);

        return this.categoryRepository.findAllWithAssociations(user.getId());
    }

    public Category findById(Long id, String username)
    {
        User user = this.userService.findByUsername(username);
        Category category = this.categoryRepository.findByIdWithAssociations(id, user.getId());

        if (category == null)
            throw new NotFoundException(String.format("The user does not have a category with the id '%d' or it does not exist", id));

        return category;
    }

    public Category findByName(String name, String username)
    {
        User user = this.userService.findByUsername(username);
        Category category = this.categoryRepository.findByName(name, user.getId());

        if (category == null)
            throw new NotFoundException("The category: '" + name + "' does not exists");

        return category;
    }

    public List<Category> findByNameCoincidence(String name, String username)
    {
        User user = this.userService.findByUsername(username);
        List<Category> categoryList = this.categoryRepository.findByNameCoincidence(name, user.getId());

        if (categoryList == null)
            throw new NotFoundException("The category: '" + name + "' does not exists");

        return categoryList;
    }

    public List<Category> findByUsername(String username)
    {
        User user = this.userService.findByUsername(username);

        return this.categoryRepository.findByUserId(user.getId());
    }

    @Transactional
    public String deleteById(Long categoryId, String username)
    {
        Category category = this.findById(categoryId, username);

        this.categoryRepository.deleteById(category.getId());

        return String.format("The category %s with the id %d was deleted successfully",
            category.getName(), categoryId);
    }

    private void checkParentCategoryById(Category category, String username, AccountCatalogue accountCatalogue)
    {
        try
        {
            if (category.getParentCategory() == null)
                return;

            Category parentCategory = this.findById(category.getParentCategory(), username);

            if (!parentCategory.getAccountCatalogue().getId().equals(accountCatalogue.getId()))
                throw new GeneralException("The type of category is not the same as the parent category");
        }
        catch(NotFoundException ex)
        {
            throw new NotFoundException("The parent Category was not found.");
        }
    }

    private void isCategoryAlreadyCreated (String categoryName, Category categoryToCompare, Long userId)
    {
        Category originalCategory = this.categoryRepository.findByName(categoryName, userId);

        if (originalCategory == null)
            return;
        else if (!compareValues(originalCategory.getId(), categoryToCompare.getId())
            && compareValues(originalCategory.getUser().getId(), userId)
        ) {
            throw new DuplicateRecordException(
                String.format(
                    "You have already created the category: %s",
                    categoryToCompare.getName()
                ));
        }
    }
    
    private boolean compareValues(Long value, Long valueToCompare)
    {
        return value.equals(valueToCompare);
    }
}
