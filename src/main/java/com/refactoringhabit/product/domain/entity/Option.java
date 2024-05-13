package com.refactoringhabit.product.domain.entity;

import com.refactoringhabit.product.domain.enums.OptionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "options")
@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private int price;

    @Enumerated(EnumType.STRING)
    private OptionStatus status; // 'SALE', 'SOLD_OUT'

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}