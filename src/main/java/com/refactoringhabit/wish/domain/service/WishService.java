package com.refactoringhabit.wish.domain.service;

import com.refactoringhabit.common.exception.CustomException;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.domain.exception.NotFoundProduct;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.wish.domain.exception.NotFoundWish;
import com.refactoringhabit.wish.domain.mapper.WishEntityMapper;
import com.refactoringhabit.wish.domain.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public String getWishAltId(String memberAltId, String productAltId) {
        try {
            return wishRepository
                .findAltIdByMemberAndProduct(getMember(memberAltId), getProduct(productAltId))
                .orElseThrow(NotFoundWish::new);
        } catch (CustomException e) {
            log.debug("[{}] ex", e.getClass().getSimpleName(), e);
            return "";
        }
    }

    private Member getMember(String memberAltId) {
        return memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);
    }

    private Product getProduct(String productAltId) {
        return productRepository.findByAltId(productAltId)
            .orElseThrow(NotFoundProduct::new);
    }
}
