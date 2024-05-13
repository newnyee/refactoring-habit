package com.refactoringhabit.order.domain.entity;


import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.order.domain.enums.PayMethod;
import com.refactoringhabit.order.domain.enums.RefundStatus;
import com.refactoringhabit.order.domain.enums.UsedStatus;
import com.refactoringhabit.product.domain.entity.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String usedAt;

    @Enumerated(EnumType.STRING)
    private PayMethod paymentMethod; // 'CARD'

    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus; // 'POSSIBLE', 'IMPOSSIBLE'

    @Enumerated(EnumType.STRING)
    private UsedStatus usedStatus; // 'UNUSED', 'USED', 'CANCELED'

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
