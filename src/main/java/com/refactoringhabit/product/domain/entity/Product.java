package com.refactoringhabit.product.domain.entity;

import com.refactoringhabit.category.domain.entity.Category;
import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.enums.ProductStatus;
import com.refactoringhabit.product.domain.enums.ProductType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "products")
@Entity
public class Product extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pid; // 외부 이용 식별자
    private String name;
    private String zip_code;
    private String address1;
    private String address2;
    private String extra_address;
    private String image;
    private String description;
    private Long view;
    private String closed_at;
    private String tag_gender;
    private String tag_age;
    private String tag_with;

    @Enumerated(EnumType.STRING)
    private ProductType type; // 'RESERVATION', 'TICKET'

    @Enumerated(EnumType.STRING)
    private ProductStatus status; // 'OPENED', 'CLOSED'

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
