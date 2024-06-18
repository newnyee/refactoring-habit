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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Table(name = "categories_large")
@Entity
public class CategoryLarge extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "eng_name")
    private String engName;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CategoryStatus status; //default 'HIDE'

    @OneToMany(mappedBy = "categoryLarge")
    private List<CategoryMiddle> categoryMiddles;
}
