package com.refactoringhabit.wish.controller;

import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.wish.domain.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/wishes")
public class WishRestController {

    private final WishService wishService;

    @PostMapping("/{productId}")
    public ApiResponse<String> createWishApi(@RequestAttribute("memberAltId") String memberAltId,
        @PathVariable("productId") String productId) {
        return ApiResponse.ok(wishService.createWish(memberAltId, productId));
    }

    @DeleteMapping("/{productId}/{wishId}")
    public ApiResponse<String> deleteWishApi(@RequestAttribute("memberAltId") String memberAltId,
        @PathVariable("productId") String productAltId, @PathVariable("wishId") String wishAltId) {
        wishService.deleteWish(memberAltId, productAltId, wishAltId);
        return ApiResponse.noContent();
    }
}
