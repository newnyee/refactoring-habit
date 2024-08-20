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
import com.refactoringhabit.cart.dto.ChooseOptionInfoDto;
import com.refactoringhabit.cart.dto.CreateCartRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Option;
import com.refactoringhabit.product.domain.repository.OptionRepository;
import java.util.List;
import java.util.Optional;
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

    @Test
    void testExistsCartsByProduct() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(cartRepository.existsByMemberAndNotEqualsProductAltId(
            member, PRODUCT_ALT_ID.getName())).thenReturn(Boolean.TRUE);

        assertTrue(cartService.existsCartsByProduct(
            MEMBER_ALT_ID.getName(), PRODUCT_ALT_ID.getName()));
    }

    @Test
    void testCartCreateOrUpdate() {
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
    void testCartCreateOrUpdate1() {
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
}
