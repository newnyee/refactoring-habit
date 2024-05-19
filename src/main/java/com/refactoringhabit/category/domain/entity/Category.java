package com.refactoringhabit.category.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import jakarta.persistence.Column;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "large")
    private String large;

    @Column(name = "middle")
    private String middle;
}
