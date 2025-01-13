package com.tenpo.controller;

import com.tenpo.dto.TransactionRequestDTO;
import com.tenpo.dto.TransactionResponseDTO;
import com.tenpo.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionControllerTest {

    // Se declara el mock del servicio, que simula la capa de servicio
    @Mock
    private TransactionService service;

    // Se declara la inyección del controlador con el mock del servicio
    @InjectMocks
    private TransactionController controller;

    // Objetos que se usan para las pruebas de los endpoints
    private TransactionRequestDTO transactionRequest;
    private TransactionResponseDTO transactionResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks de Mockito

        // Inicializa el objeto DTO de solicitud (para crear una transacción)
        transactionRequest = TransactionRequestDTO.builder()
                .customer("1")
                .amount(1000)
                .merchant("StoreA")
                .date(OffsetDateTime.now())
                .build();

        // Inicializa el objeto DTO de respuesta (para las transacciones devueltas por el servicio)
        transactionResponse = TransactionResponseDTO.builder()
                .id(1)
                .customer("1")
                .amount(1000)
                .merchant("StoreA")
                .date(OffsetDateTime.now())
                .build();
    }

    // Test para verificar el comportamiento de obtener todas las transacciones
    @Test
    void getAllTransactions() {
        // Configura el mock para que el servicio devuelva una lista con una transacción
        when(service.getAllTransactions()).thenReturn(Collections.singletonList(transactionResponse));

        // Llama al método del controlador para obtener todas las transacciones
        ResponseEntity<List<TransactionResponseDTO>> response = controller.getAllTransactions();

        // Verifica que el código de estado sea OK (200) y que la lista tenga un solo elemento
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

        // Verifica que el servicio haya sido llamado una vez
        verify(service, times(1)).getAllTransactions();
    }

    // Test para verificar el comportamiento de crear una transacción
    @Test
    void createTransaction() {
        // Configura el mock para que el servicio devuelva la transacción creada
        when(service.createTransaction(any())).thenReturn(transactionResponse);

        // Llama al método del controlador para crear una transacción
        ResponseEntity<TransactionResponseDTO> response = controller.createTransaction(transactionRequest);

        // Verifica que el código de estado sea CREATED (201) y que la respuesta sea la transacción creada
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transactionResponse, response.getBody());

        // Verifica que el servicio haya sido llamado una vez con cualquier argumento
        verify(service, times(1)).createTransaction(any());
    }

    // Test para verificar el comportamiento de actualizar una transacción
    @Test
    void updateTransaction() {
        // Configura el mock para que el servicio devuelva la transacción actualizada
        when(service.updateTransaction(eq(1), any())).thenReturn(transactionResponse);

        // Llama al método del controlador para actualizar la transacción con id 1
        ResponseEntity<TransactionResponseDTO> response = controller.updateTransaction(1, transactionRequest);

        // Verifica que el código de estado sea OK (200) y que la respuesta sea la transacción actualizada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionResponse, response.getBody());

        // Verifica que el servicio haya sido llamado una vez con el id 1 y cualquier argumento
        verify(service, times(1)).updateTransaction(eq(1), any());
    }

    // Test para verificar el comportamiento de eliminar una transacción
    @Test
    void deleteTransaction() {
        // Configura el mock para que el servicio no haga nada al eliminar la transacción
        doNothing().when(service).deleteTransaction(1);

        // Llama al método del controlador para eliminar la transacción con id 1
        ResponseEntity<Void> response = controller.deleteTransaction(1);

        // Verifica que el código de estado sea NO_CONTENT (204) al eliminar con éxito
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verifica que el servicio haya sido llamado una vez con el id 1
        verify(service, times(1)).deleteTransaction(1);
    }
}
