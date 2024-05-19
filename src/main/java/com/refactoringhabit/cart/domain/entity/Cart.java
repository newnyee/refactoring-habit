package com.refactoringhabit.cart.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "carts")
@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "quantity")
    private int quantity;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;
}
