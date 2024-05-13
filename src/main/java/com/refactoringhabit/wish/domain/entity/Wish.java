package com.refactoringhabit.wish.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "wishes")
@Entity
public class Wish extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "id")
    private Product product;
}
