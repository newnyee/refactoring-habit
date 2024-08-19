package com.refactoringhabit.cart.domain.service;

import com.refactoringhabit.cart.domain.entity.Cart;
import com.refactoringhabit.cart.domain.mapper.CartEntityMapper;
import com.refactoringhabit.cart.domain.repository.CartRepository;
import com.refactoringhabit.cart.dto.ChooseOptionInfoDto;
import com.refactoringhabit.cart.dto.CreateCartRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Option;
import com.refactoringhabit.product.domain.exception.NotFoundOptionException;
import com.refactoringhabit.product.domain.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;

    private static final String OPTION_ALT_ID = null;

    @Transactional(readOnly = true)
    public boolean existsCartsByProduct(String memberAltId, String productAltId) {
        return cartRepository.existsByMemberAndNotEqualsProductAltId(getMember(memberAltId),
            productAltId);
    }

    @Transactional
    public void cartCreate(String memberAltId, CreateCartRequestDto createCartRequestDto) {
        Member member = getMember(memberAltId);

        if (Boolean.TRUE.equals(createCartRequestDto.getShouldDeleteCart())) {
            cartRepository.deleteByMember(member);
        }

        for (ChooseOptionInfoDto chooseOptionInfoDto : createCartRequestDto.getChooseOptionInfoDtos()) {
            Option option = getOption(chooseOptionInfoDto.getOptionAltId());
            Cart cart = cartRepository.findByMemberAndOption(member, option);

            if (cart != null) {
                cart.setQuantity(chooseOptionInfoDto.getQuantity());
            } else {
                cartRepository.save(CartEntityMapper.INSTANCE
                    .toEntity(member, option, chooseOptionInfoDto.getQuantity(), OPTION_ALT_ID));
            }
        }
    }

    private Option getOption(String optionAltId) {
        return optionRepository.findByAltId(optionAltId)
            .orElseThrow(NotFoundOptionException::new);
    }

    private Member getMember(String memberAltId) {
        return memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);
    }
}
