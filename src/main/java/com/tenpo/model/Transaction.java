package com.tenpo.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    private String customer;

    @NotNull
    private int amount;

    @NotEmpty
    private String merchant;

    @NotNull
    private OffsetDateTime date; //OffsetDateTime zona horaria
}
