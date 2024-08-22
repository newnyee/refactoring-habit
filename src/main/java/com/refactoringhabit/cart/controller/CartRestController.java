package com.refactoringhabit.cart.controller;

import com.refactoringhabit.cart.domain.service.CartService;
import com.refactoringhabit.cart.dto.CreateCartRequestDto;
import com.refactoringhabit.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/carts")
public class CartRestController {

    private final CartService cartService;

    @GetMapping("/products/{productId}/exists")
    public ApiResponse<Boolean> existsCartsByProductApi(
        @RequestAttribute("memberAltId") String memberAltId,
        @PathVariable("productId") String productAltId) {
        return ApiResponse.ok(cartService.existsCartsByProduct(memberAltId, productAltId));
    }

    @PutMapping
    public ApiResponse<String> createOrUpdateCartApi(
        @RequestAttribute("memberAltId") String memberAltId,
        @RequestBody CreateCartRequestDto createCartRequestDto) {
        cartService.cartCreateOrUpdate(memberAltId, createCartRequestDto);
        return ApiResponse.created();
    }

    @PatchMapping("/{cartId}")
    public ApiResponse<String> updateCartApi(
        @RequestAttribute("memberAltId") String memberAltId,
        @PathVariable("cartId") String cartAltId, @RequestBody int quantity) {
        cartService.updateCart(memberAltId, cartAltId, quantity);
        return ApiResponse.noContent();
    }

    @DeleteMapping("/{cartId}")
    public ApiResponse<String> deleteCartApi(
        @RequestAttribute("memberAltId") String memberAltId,
        @PathVariable("cartId") String cartAltId) {
        cartService.deleteCart(memberAltId, cartAltId);
        return ApiResponse.noContent();
    }
}
