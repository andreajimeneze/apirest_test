package cl.kibernum.apirest.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.kibernum.apirest.security.domain.*;
import cl.kibernum.apirest.exception.ResourceNotFoundException;
import cl.kibernum.apirest.security.services.UserAccountServiceImpl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/UserAccounts")
public class UserAccountController {
    private UserAccountServiceImpl UserAccountService;

    public UserAccountController(UserAccountServiceImpl UserAccountService) {
        this.UserAccountService = UserAccountService;
    }

    @GetMapping
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        return ResponseEntity.ok(UserAccountService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getUserById(@RequestParam Long id) {
        UserAccount UserAccount = UserAccountService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount no encontrado"));

        return ResponseEntity.ok(UserAccount);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<Void> softDeleteUserAccount(@PathVariable String email) {
        UserAccount UserAccount = UserAccountService.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount no encontrado"));

        UserAccountService.softDelete(UserAccount.getId());
        return ResponseEntity.noContent().build();
    }

}
