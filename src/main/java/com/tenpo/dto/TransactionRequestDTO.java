package com.tenpo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.OffsetDateTime;

@Data
@Builder
@ToString
public class TransactionRequestDTO {

    @Schema(
        name = "customer",
        description = "Identificador único del cliente asociado a la transacción.",
        example = "1",
        required = true
    )
    private String customer;

    @Schema(
        name = "amount",
        description = "Monto total de la transacción en centavos o la unidad mínima de la moneda.",
        example = "1000",
        required = true
    )
    private Integer amount;

    @Schema(
        name = "merchant",
        description = "Nombre o identificador del comerciante donde se realizó la transacción.",
        example = "StoreA",
        required = true
    )
    private String merchant;

    @Schema(
        name = "date",
        description = "Fecha y hora de la transacción en formato ISO 8601 con zona horaria.",
        example = "2025-01-13T12:00:00Z",
        required = true
    )
    private OffsetDateTime date;
}
