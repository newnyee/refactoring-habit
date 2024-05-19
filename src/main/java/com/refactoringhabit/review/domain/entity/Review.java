package com.refactoringhabit.review.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Option;
import com.refactoringhabit.review.domain.enums.ReviewStatus;
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

@Table(name = "reviews")
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "content")
    private String content;

    @Column(name = "star_score")
    private int starScore;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReviewStatus status; // default 'SHOW'

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;
}
