package com.refactoringhabit.invoice.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.invoice.domain.enums.InvoiceStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// 정산 테이블
@Table(name = "invoices")
@Entity
public class Invoice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String period; // 정산 기간
    private String title; // 정산서 제목
    private int paymentAmount; // 지급액
    private String accountNumber; // 정산 계좌 번호
    private  String accountHolder; // 예금주
    private String bank; // 정산 은행 이름

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status; // 정산 지급 상태 - 'WAIT', 'COMPLETE'

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;
}
