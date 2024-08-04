package com.refactoringhabit.category.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseCreateTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "categories_middle")
public class CategoryMiddle extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "name")
    private String name;

    @Column(name = "eng_name")
    private String engName;

    @ManyToOne
    @JoinColumn(name = "category_large_id")
    private CategoryLarge categoryLarge;
}
