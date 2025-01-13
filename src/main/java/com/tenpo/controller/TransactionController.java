package com.tenpo.controller;

import com.tenpo.dto.TransactionRequestDTO;
import com.tenpo.dto.TransactionResponseDTO;
import com.tenpo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    // Retorna todas las transacciones
    @GetMapping("/allTransactions")
    @Operation(summary = "Get All Transactions", description = "Retrieve a list of all transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        log.info("Obteniendo todas las transacciones");
        var transactions = service.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    // Crea una nueva transacción
    @PostMapping("/create")
    @Operation(summary = "Create Transaction", description = "Used to create a transaction")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO request) {
        log.info("Creando una nueva transacción para el cliente {}", request.getCustomer());
        var createdTransaction = service.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    // Actualiza una transacción
    @PutMapping("/update/{id}")
    @Operation(summary = "Update Transaction", description = "Used to update a transaction")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(@PathVariable int id, @Valid @RequestBody TransactionRequestDTO request) {
        log.info("Actualizando transacción con ID {}", id);
        var updatedTransaction = service.updateTransaction(id, request);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Elimina una transacción
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Transaction", description = "Used to delete a transaction")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id) {
        log.info("Eliminando transacción con ID {}", id);
        service.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}

