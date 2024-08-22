package com.refactoringhabit.cart.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.PRODUCT_ALT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.cart.domain.entity.Cart;
import com.refactoringhabit.cart.domain.repository.CartRepository;
import com.refactoringhabit.cart.dto.CartDetailResponseDto;
import com.refactoringhabit.cart.dto.ChooseOptionInfoDto;
import com.refactoringhabit.cart.dto.CreateCartRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Option;
import com.refactoringhabit.product.domain.repository.OptionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private Member member;

    @Mock
    private Option option;

    @Mock
    private Cart cart;

    private static final String OPTION_ALT_ID = "optionAltId";
    private static final String CART_ALT_ID = "cartAltId";

    @Test
    @DisplayName("특정 상품의 옵션이 카트에 존재하는지 확인")
    void testExistsCartsByProduct() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(cartRepository.existsByMemberAndNotEqualsProductAltId(
            member, PRODUCT_ALT_ID.getName())).thenReturn(Boolean.TRUE);

        assertTrue(cartService.existsCartsByProduct(
            MEMBER_ALT_ID.getName(), PRODUCT_ALT_ID.getName()));
    }

    @Test
    @DisplayName("카트에 옵션 등록 또는 수정 - 카트에 다른 상품 존재, 카트에 옵션 등록")
    void testCartCreateOrUpdate_ExistsCartAndCreateCart() {
        CreateCartRequestDto createCartRequestDto = mock(CreateCartRequestDto.class);
        ChooseOptionInfoDto chooseOptionInfoDto = mock(ChooseOptionInfoDto.class);
        List<ChooseOptionInfoDto> chooseOptionInfoDtos = List.of(chooseOptionInfoDto);
        int getQuantity = 1;

        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(createCartRequestDto.getShouldDeleteCart()).thenReturn(Boolean.TRUE);
        when(createCartRequestDto.getChooseOptionInfoDtos()).thenReturn(chooseOptionInfoDtos);
        when(chooseOptionInfoDto.getOptionAltId()).thenReturn(OPTION_ALT_ID);
        when(optionRepository.findByAltId(OPTION_ALT_ID)).thenReturn(Optional.of(option));
        when(cartRepository.findByMemberAndOption(member, option)).thenReturn(cart);
        when(chooseOptionInfoDto.getQuantity()).thenReturn(getQuantity);

        cartService.cartCreateOrUpdate(MEMBER_ALT_ID.getName(), createCartRequestDto);
        verify(cartRepository).deleteByMember(member);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("카트에 옵션 등록 또는 수정 - 카트에 다른 상품 없음, 카트의 옵션 수정")
    void testCartCreateOrUpdate_NotExistsCartAndUpdateCart() {
        CreateCartRequestDto createCartRequestDto = mock(CreateCartRequestDto.class);
        ChooseOptionInfoDto chooseOptionInfoDto = mock(ChooseOptionInfoDto.class);
        List<ChooseOptionInfoDto> chooseOptionInfoDtos = List.of(chooseOptionInfoDto);
        int getQuantity = 1;

        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(createCartRequestDto.getShouldDeleteCart()).thenReturn(Boolean.FALSE);
        when(createCartRequestDto.getChooseOptionInfoDtos()).thenReturn(chooseOptionInfoDtos);
        when(chooseOptionInfoDto.getOptionAltId()).thenReturn(OPTION_ALT_ID);
        when(optionRepository.findByAltId(OPTION_ALT_ID)).thenReturn(Optional.of(option));
        when(cartRepository.findByMemberAndOption(member, option)).thenReturn(null);
        when(chooseOptionInfoDto.getQuantity()).thenReturn(getQuantity);

        cartService.cartCreateOrUpdate(MEMBER_ALT_ID.getName(), createCartRequestDto);
        verify(cartRepository, never()).deleteByMember(member);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("회원에 따른 장바구니 목록 가져오기")
    void testGetCartsByMember() {
        List<CartDetailResponseDto> cartDetailResponseDtos = mock(List.class);
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(cartRepository.getCartDetailsByMember(member)).thenReturn(cartDetailResponseDtos);

        List<CartDetailResponseDto> getCartDetailResponseDtos =
            cartService.getCartsByMember(MEMBER_ALT_ID.getName());
        Assertions.assertEquals(cartDetailResponseDtos, getCartDetailResponseDtos);
    }

    @Test
    @DisplayName("회원에 따른 장바구니 목록 삭제하기")
    void testDeleteCartByMember() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));

        cartService.deleteCartsByMember(MEMBER_ALT_ID.getName());
        verify(cartRepository).deleteByMember(member);
    }

    @Test
    @DisplayName("장바구니의 특정 옵션 수정")
    void testUpdateCart() {
        int quantity = 1;
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(cartRepository.findByMemberAndAltId(member, CART_ALT_ID))
            .thenReturn(Optional.of(cart));

        cartService.updateCart(MEMBER_ALT_ID.getName(), CART_ALT_ID, quantity);
        verify(cart).setQuantity(quantity);
    }

    @Test
    @DisplayName("장바구니의 특정 옵션 삭제")
    void testDeleteCart() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));

        cartService.deleteCart(MEMBER_ALT_ID.getName(), CART_ALT_ID);
        verify(cartRepository).deleteByMemberAndAltId(member, CART_ALT_ID);
    }
}
