package com.refactoringhabit.product.domain.repository;

import static com.refactoringhabit.product.domain.entity.QOption.option;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refactoringhabit.product.domain.enums.OptionStatus;
import com.refactoringhabit.product.dto.OptionDetailDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OptionRepositoryImpl implements OptionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OptionDetailDto> getOptionDetailDtos(String productAltId) {
        return jpaQueryFactory
            .select(Projections.constructor(OptionDetailDto.class,
                option.altId,
                option.name,
                option.price,
                option.quantity,
                option.product.type.stringValue().as("productType")))
            .from(option)
            .where(option.product.altId.eq(productAltId)
                .and(option.status.eq(OptionStatus.SALE)))
            .fetch();
    }
}
