package com.test.microservices.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_inventory")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String skuCode;
    private Integer quantity;
}
