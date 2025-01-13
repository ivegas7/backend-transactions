package com.tenpo.service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import com.tenpo.dto.TransactionRequestDTO;
import com.tenpo.dto.TransactionResponseDTO;
import com.tenpo.errors.InternalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.tenpo.model.Transaction;
import com.tenpo.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import com.tenpo.errors.BadRequestException;
import com.tenpo.errors.ResourceNotFoundException;
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // Devuelve la lista de todas las transacciones
    public List<TransactionResponseDTO> getAllTransactions() {
        log.info("Listando todas las transacciones");
        var transactions = transactionRepository.findAll();

        if (transactions.isEmpty()) {
            log.warn("No se encontraron transacciones");
            return Collections.emptyList();
        }

        return transactions.stream()
                .map(this::getTransactionResponseDTO)
                .toList();
    }

    // Devuelve una transacción específica por ID
    public Transaction getTransactionById(int id) {
        log.info("Buscando la transacción con ID {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
    }

    // Crea una nueva transacción
    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO request) {
        log.info("Creando transacción para el cliente {}", request.getCustomer());
        validateTransaction(request);

        var transaction = new Transaction();
        transaction.setCustomer(request.getCustomer());
        transaction.setAmount(request.getAmount());
        transaction.setMerchant(request.getMerchant());
        transaction.setDate(request.getDate());

        try {
            transactionRepository.save(transaction);
        } catch (Exception e) {
            log.error("Error al guardar la transacción: {}", e.getMessage());
            throw new InternalErrorException("An error occurred while saving the transaction");
        }

        return getTransactionResponseDTO(transaction);
    }

    // Actualiza una transacción existente
    @Transactional
    public TransactionResponseDTO updateTransaction(int id, TransactionRequestDTO updatedTransaction) {
        log.info("Actualizando transacción con ID {}", id);

        var existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        validateTransaction(updatedTransaction);

        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setMerchant(updatedTransaction.getMerchant());
        existingTransaction.setCustomer(updatedTransaction.getCustomer());
        existingTransaction.setDate(updatedTransaction.getDate());

        try {
            transactionRepository.save(existingTransaction);
        } catch (Exception e) {
            log.error("Error al actualizar la transacción con ID {}: {}", id, e.getMessage());
            throw new InternalErrorException("An error occurred while updating the transaction");
        }

        return getTransactionResponseDTO(existingTransaction);
    }

    // Elimina una transacción
    @Transactional
    public void deleteTransaction(int id) {
        log.info("Buscando transacción con ID {}", id);

        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        try {
            transactionRepository.delete(transaction);
        } catch (Exception e) {
            log.error("Error al eliminar la transacción con ID {}: {}", id, e.getMessage());
            throw new InternalErrorException("An error occurred while deleting the transaction.");
        }

        log.info("Transacción con ID {} eliminada exitosamente", id);
    }

    // Validaciones de la transacción
    private void validateTransaction(TransactionRequestDTO request) {
        if (request.getDate().isAfter(OffsetDateTime.now())) {
            throw new BadRequestException("The date cannot be later than the current date");
        }

        if (request.getAmount() <= 0) {
            throw new BadRequestException("The amount must be greater than zero");
        }

        long count = transactionRepository.countByCustomer(request.getCustomer());
        if (count >= 100) {
            throw new BadRequestException("No more than 100 transactions can be recorded");
        }
    }

    // Convierte una transacción en su DTO de respuesta
    private TransactionResponseDTO getTransactionResponseDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .customer(transaction.getCustomer())
                .amount(transaction.getAmount())
                .merchant(transaction.getMerchant())
                .date(transaction.getDate())
                .build();
    }
}
