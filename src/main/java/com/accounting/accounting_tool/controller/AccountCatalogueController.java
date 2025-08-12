package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.dto.account_catalogue.AccountCatalogueDTO;
import com.accounting.accounting_tool.entity.AccountCatalogue;
import com.accounting.accounting_tool.response.BasicResponse;
import com.accounting.accounting_tool.service.AccountCatalogueService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/account-catalogue")
public class AccountCatalogueController
{
    private AccountCatalogueService accountCatalogueService;
    private ModelMapper modelMapper;

    @Autowired
    private AccountCatalogueController(AccountCatalogueService accountCatalogueService, ModelMapper modelMapper)
    {
        this.accountCatalogueService = accountCatalogueService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create")
    private ResponseEntity<?> save (@Valid @RequestBody AccountCatalogueDTO accountCatalogueCreateDTO)
    {
        AccountCatalogue newAccountCatalogue = this.modelMapper.map(accountCatalogueCreateDTO, AccountCatalogue.class);

        newAccountCatalogue = this.accountCatalogueService.save(newAccountCatalogue);

        return new ResponseEntity<>(newAccountCatalogue, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<?> update (@Valid @RequestBody AccountCatalogueDTO accountCatalogueCreateDTO, @PathVariable Long id)
    {
        AccountCatalogue accountCatalogue = this.modelMapper.map(accountCatalogueCreateDTO, AccountCatalogue.class);
        accountCatalogue.setId(id);

        return new ResponseEntity<>(this.accountCatalogueService.update(accountCatalogue), HttpStatus.OK);
    }

    @GetMapping("/")
    private ResponseEntity<?> findAll()
    {
        List<AccountCatalogue> accountCatalogueList = this.accountCatalogueService.findAll();

        return new ResponseEntity<>(accountCatalogueList, HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    private ResponseEntity<?> findById(@PathVariable Long id)
    {
        AccountCatalogue accountCatalogueList = this.accountCatalogueService.findById(id);

        return new ResponseEntity<>(accountCatalogueList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> delete (@PathVariable Long id)
    {
        String message = this.accountCatalogueService.deleteById(id);
        BasicResponse<String> response = new BasicResponse<>(message, "successful");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
