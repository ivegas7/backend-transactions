package com.tenpo.service;

import com.tenpo.dto.TransactionRequestDTO;
import com.tenpo.errors.ResourceNotFoundException;
import com.tenpo.model.Transaction;
import com.tenpo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        // Initialize the transaction object
        transaction = new Transaction();
        transaction.setId(1);
        transaction.setAmount(500);
        transaction.setMerchant("Supermercado");
        transaction.setCustomer("Juan");

        OffsetDateTime specificDate = OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        transaction.setDate(specificDate);
    }

    @Test
    void testCreateTransaction() {
        // Configurar mock para devolver la transacción creada cuando se llame a save()
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        var response = transactionService.createTransaction(getTransactionRequestDTO());

        // Verifica que la transacción no sea nula
        assertNotNull(response);
        // Verifica que el monto de la transacción creada sea 500
        assertEquals(500, response.getAmount());
        // Verifica que el método save() haya sido llamado una vez con el objeto transaction
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testUpdateTransaction() {
        OffsetDateTime updatedDate = OffsetDateTime.of(2025, 1, 1, 11, 0, 0, 0, ZoneOffset.UTC);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(1);
        updatedTransaction.setAmount(700);
        updatedTransaction.setMerchant("Tienda");
        updatedTransaction.setCustomer("Carlos");
        updatedTransaction.setDate(updatedDate);

        // Configura el mock para que encuentre la transacción con id 1 y devuelva la transacción original
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        // Configura el mock para que guarde la transacción actualizada
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        // Llama al método de servicio para actualizar la transacción
        var response = transactionService.updateTransaction(1, getTransactionRequestDTO());

        // Verifica que el monto de la transacción actualizada sea 700
        assertEquals(500, response.getAmount());
        // Verifica que el comercio de la transacción actualizada sea "Tienda"
        assertEquals("Supermercado", response.getMerchant());
        // Verifica que el método save() haya sido llamado una vez con el objeto updatedTransaction
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testDeleteTransaction() {
        // Configura el mock para que encuentre la transacción con id 1
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        // Configura el mock para eliminar la transacción
        doNothing().when(transactionRepository).delete(any(Transaction.class));

        // Llama al método de servicio para eliminar la transacción
        transactionService.deleteTransaction(1);

        // Verifica que el método delete() haya sido llamado una vez con el objeto transaction
        verify(transactionRepository, times(1)).delete(any(Transaction.class));
    }

    @Test
    void testDeleteTransactionNotFound() {
        // Configura el mock para indicar que la transacción con id 1 no existe
        when(transactionRepository.findById(1)).thenReturn(Optional.empty());

        // Se espera que se lance una excepción de tipo ResourceNotFoundException cuando no se encuentre la transacción
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.deleteTransaction(1);
        });

        // Verifica que el mensaje de la excepción sea "Transacción no encontrada con ID: 1"
        assertEquals("Transacción no encontrada con ID: 1", exception.getMessage());
    }

    private TransactionRequestDTO getTransactionRequestDTO() {
        return TransactionRequestDTO.builder()
                .customer("nacho")
                .amount(500)
                .merchant("Supermercado")
                .date(OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC))
                .build();
    }

}
