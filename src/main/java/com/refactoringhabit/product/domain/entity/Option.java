package com.refactoringhabit.product.domain.entity;

import com.refactoringhabit.product.domain.enums.OptionStatus;
import jakarta.persistence.Column;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OptionStatus status; // default 'SALE'

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
