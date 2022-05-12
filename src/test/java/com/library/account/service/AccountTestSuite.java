package com.library.account.service;

import com.library.LibraryApplication;
import com.library.account.entity.Account;
import com.library.account.repository.AccountRepository;
import com.library.testcontainers.config.ContainerEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.library.account.factory.AccountCreator.admin;
import static com.library.account.factory.AccountCreator.member;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = LibraryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AccountTestSuite extends ContainerEnvironment {
    
    
    @Autowired
    private AccountRepository accountRepository;
    
    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }
    
    @Test
    void shouldReturnEmptyAccountList() {
        // given & when
        List<Account> accountList = accountRepository.findAll();
        
        // then
        assertEquals(0, accountList.size());
    }
    
    @Test
    void shouldReturnNotEmptyAccountList() {
        // given & when
        accountRepository.save(admin());
        List<Account> accountList = accountRepository.findAll();
        
        // then
        assertEquals(1, accountList.size());
    }
    
    @Test
    void shouldFetchSavedAccountByLogin() {
        // given & when
        final Account savedAccount = accountRepository.save(admin());
        final Account fetchedAccount = accountRepository.findByLogin(savedAccount.getLogin());
        
        // then
        assertEquals(savedAccount.getLogin(), fetchedAccount.getLogin());
    }
    
    @Test
    void shouldReturnTheSizeOfAccountRepositoryAfterDeleteById() {
        // given & when
        final Account member1 = accountRepository.save(member());
        final Account member2 = accountRepository.save(member());
        
        List<Account> accountList = accountRepository.findAll();
        
        // then
        assertEquals(2, accountList.size());
        
        accountRepository.deleteById(member1.getId());
        accountList = accountRepository.findAll();
        assertEquals(1, accountList.size());
    }
}