package com.tenpo.repository;

import com.tenpo.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties") 
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction transaction;
    
    // Método que se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {
    	// Se crea y configura una nueva transacción
    	OffsetDateTime specificDate = OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        transaction = new Transaction();
        transaction.setAmount(1000);
        transaction.setMerchant("Supermercado");
        transaction.setCustomer("Pedro");
        transaction.setDate(specificDate);
    }

    @Test
    void testSaveTransaction() {
    	// Se guarda la transacción en la base de datos
        Transaction savedTransaction = transactionRepository.save(transaction);
        // Verifica que la transacción guardada no sea nula
        assertNotNull(savedTransaction);
        // Verifica que el monto de la transacción guardada sea el mismo que el monto de la transacción original
        assertEquals(transaction.getAmount(), savedTransaction.getAmount());
    }

    @Test
    void testFindTransactionById() {
    	// Se guarda la transacción en la base de datos
        Transaction savedTransaction = transactionRepository.save(transaction);
        // Luego, se busca la transacción por su ID
        Optional<Transaction> foundTransaction = transactionRepository.findById(savedTransaction.getId());
        // Verifica que la transacción encontrada esté presente en la base de datos
        assertTrue(foundTransaction.isPresent());
     // Verifica que el monto de la transacción encontrada sea el mismo que el monto de la transacción guardada
        assertEquals(savedTransaction.getAmount(), foundTransaction.get().getAmount());
    }

    @Test
    void testDeleteTransaction() {
    	// Se guarda la transacción en la base de datos
        Transaction savedTransaction = transactionRepository.save(transaction);
     // Se elimina la transacción utilizando su ID
        transactionRepository.deleteById(savedTransaction.getId());
        // Luego, se intenta encontrar la transacción eliminada
        Optional<Transaction> deletedTransaction = transactionRepository.findById(savedTransaction.getId());
     // Verifica que la transacción eliminada no esté presente en la base de datos
        assertFalse(deletedTransaction.isPresent());
    }
}
