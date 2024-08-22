package com.refactoringhabit.wish.domain.service;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.domain.exception.NotFoundProductException;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.wish.domain.mapper.WishEntityMapper;
import com.refactoringhabit.wish.domain.repository.WishRepository;
import com.refactoringhabit.wish.dto.WishesInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public static final String WISH_ALT_ID = null;

    @Transactional
    public String createWish(String memberAltId, String productAltId) {
        return wishRepository
            .save(WishEntityMapper.INSTANCE
                .toEntity(getMember(memberAltId), getProduct(productAltId), WISH_ALT_ID))
            .getAltId();
    }

    @Transactional
    public void deleteWish(String memberAltId, String productAltId, String wishAltId) {
        wishRepository.deleteByMemberAndProductAndAltId(
            getMember(memberAltId), getProduct(productAltId), wishAltId);
    }

    @Transactional(readOnly = true)
    public WishesInfoResponseDto getWishesInfo(String memberAltId, Pageable pageable) {
        Member member = getMember(memberAltId);
        return WishesInfoResponseDto.builder()
            .wishes(wishRepository.getWishesByMember(member, pageable))
            .wishCount(wishRepository.countByMember(member))
            .build();
    }

    private Member getMember(String memberAltId) {
        return memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);
    }

    private Product getProduct(String productAltId) {
        return productRepository.findByAltId(productAltId)
            .orElseThrow(NotFoundProductException::new);
    }
}
