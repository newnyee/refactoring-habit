package com.refactoringhabit.product.domain.entity;

import com.refactoringhabit.category.domain.entity.Category;
import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.enums.ProductStatus;
import com.refactoringhabit.product.domain.enums.ProductType;
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

@Table(name = "products")
@Entity
public class Product extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "alt_id")
    private String altId; // 대체키
    @Column(name = "name")
    private String name;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "address1")
    private String address1;
    @Column(name = "address2")
    private String address2;
    @Column(name = "extra_address")
    private String extraAddress;
    @Column(name = "image")
    private String image;
    @Column(name = "description")
    private String description;
    @Column(name = "view")
    private Long view;
    @Column(name = "closed_at")
    private String closedAt;
    @Column(name = "tag_gender")
    private String tagGender;
    @Column(name = "tag_age")
    private String tagAge;
    @Column(name = "tag_with")
    private String tagWith;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status; // default 'OPENED'

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
