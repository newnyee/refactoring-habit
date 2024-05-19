package com.refactoringhabit.category.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "categories")
@Entity
public class Category extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String altId; // 대체키
    private String large;
    private String middle;
}
