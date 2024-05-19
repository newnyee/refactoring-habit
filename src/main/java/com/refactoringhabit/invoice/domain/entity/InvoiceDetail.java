package com.refactoringhabit.invoice.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import com.refactoringhabit.product.domain.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "invoice_details")
@Entity
public class InvoiceDetail extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String altId; // 대체키
    private int totalPrice;
    private int totalQuantity;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
