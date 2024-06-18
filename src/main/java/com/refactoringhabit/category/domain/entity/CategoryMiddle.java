package com.refactoringhabit.category.domain.entity;

import com.refactoringhabit.category.domain.enums.CategoryStatus;
import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
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

@Table(name = "categories_middle")
@Entity
public class CategoryMiddle extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CategoryStatus status; //default 'HIDE'

    @ManyToOne
    @JoinColumn(name = "category_large_id")
    private CategoryLarge categoryLarge;
}
