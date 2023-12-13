package com.app.api;

import com.app.payload.request.AccountQueryParam;
import com.app.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountApi {
    @Autowired
    AccountService accountService;

    @GetMapping("/admin/accounts")
    public ResponseEntity<?> getAllAccount(AccountQueryParam accountQueryParam) {
        try {
            return ResponseEntity.ok(accountService.filterAccount(accountQueryParam));
        } catch (Exception e) {
            // Handle the exception or log the error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PutMapping("/admin/accounts/{id}")
    public ResponseEntity<?> blockAccount(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(accountService.blockAccount(id));
        } catch (Exception e) {
            // Handle the exception or log the error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }

    @DeleteMapping("/admin/accounts/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(accountService.removeAccount(id));
        } catch (Exception e) {
            // Handle the exception or log the error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

}
