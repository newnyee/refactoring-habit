package com.refactoringhabit.order.domain.entity;


import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.product.domain.entity.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String oid; // 외부 이용 식별자
    private int price;
    private int quantity;
    private Enum paymentMethod; // 'CARD'
    private Enum refundStatus; // 'POSSIBLE', 'IMPOSSIBLE'
    private Enum usedStatus; // 'UNUSED', 'USED', 'CANCELED'
    private String usedAt;

    @ManyToOne
    @JoinColumn(name = "id")
    private Host host;

    @ManyToOne
    @JoinColumn(name = "id")
    private Option option;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;
}
