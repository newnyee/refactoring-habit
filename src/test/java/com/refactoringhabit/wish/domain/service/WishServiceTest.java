package com.refactoringhabit.wish.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.PRODUCT_ALT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.wish.domain.entity.Wish;
import com.refactoringhabit.wish.domain.exception.NotFoundWish;
import com.refactoringhabit.wish.domain.repository.WishRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Wish wish;

    @Mock
    private Member member;

    @Mock
    private Product product;

    @InjectMocks
    private WishService wishService;

    private static final String WISH_ALT_ID = "wishAltId";
    private static final String WISH_ALT_ID_IS_NULL = "";

    @Test
    @DisplayName("찜 대체키 얻기 - 성공")
    void testGetWishAltId_Success() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(productRepository.findByAltId(PRODUCT_ALT_ID.getName()))
            .thenReturn(Optional.of(product));
        when(wishRepository.findAltIdByMemberAndProduct(member, product))
            .thenReturn(Optional.of(WISH_ALT_ID));

        String getWishAltId =
            wishService.getWishAltId(MEMBER_ALT_ID.getName(), PRODUCT_ALT_ID.getName());
        assertEquals(WISH_ALT_ID, getWishAltId);
    }

    @Test
    @DisplayName("찜 대체키 얻기 - 실패 : 찾을 수 없는 찜")
    void testGetWishAltId_NotFoundWish() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(productRepository.findByAltId(PRODUCT_ALT_ID.getName()))
            .thenReturn(Optional.of(product));
        when(wishRepository.findAltIdByMemberAndProduct(member, product))
            .thenThrow(NotFoundWish.class);

        String getWishAltId =
            wishService.getWishAltId(MEMBER_ALT_ID.getName(), PRODUCT_ALT_ID.getName());
        assertEquals(WISH_ALT_ID_IS_NULL, getWishAltId);
    }

    @Test
    @DisplayName("찜 생성")
    void testCreateWish() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(productRepository.findByAltId(PRODUCT_ALT_ID.getName()))
            .thenReturn(Optional.of(product));
        when(wishRepository.save(any())).thenReturn(wish);
        when(wish.getAltId()).thenReturn(WISH_ALT_ID);

        String getWishAltId =
            wishService.createWish(MEMBER_ALT_ID.getName(), PRODUCT_ALT_ID.getName());
        assertEquals(WISH_ALT_ID, getWishAltId);
    }

    @Test
    @DisplayName("찜 삭제")
    void testDeleteWish() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(productRepository.findByAltId(PRODUCT_ALT_ID.getName()))
            .thenReturn(Optional.of(product));

        wishService.deleteWish(MEMBER_ALT_ID.getName(), PRODUCT_ALT_ID.getName(), WISH_ALT_ID);
        verify(wishRepository).deleteByMemberAndProductAndAltId(member, product, WISH_ALT_ID);
        verify(memberRepository).findByAltId(MEMBER_ALT_ID.getName());
        verify(productRepository).findByAltId(PRODUCT_ALT_ID.getName());
    }
}
