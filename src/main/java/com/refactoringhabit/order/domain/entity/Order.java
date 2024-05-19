package com.refactoringhabit.order.domain.entity;


import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.order.domain.enums.PayMethod;
import com.refactoringhabit.order.domain.enums.RefundStatus;
import com.refactoringhabit.order.domain.enums.UsedStatus;
import com.refactoringhabit.product.domain.entity.Option;
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

@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "price")
    private int price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "used_at")
    private String usedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PayMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status")
    private RefundStatus refundStatus; // default 'POSSIBLE'

    @Enumerated(EnumType.STRING)
    @Column(name = "used_status")
    private UsedStatus usedStatus; // default 'UNUSED'

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
