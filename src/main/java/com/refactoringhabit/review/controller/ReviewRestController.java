package com.refactoringhabit.review.controller;

import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.review.domain.service.ReviewService;
import com.refactoringhabit.review.dto.ReviewUpdateRequestDto;
import com.refactoringhabit.review.dto.ReviewUpdateResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewUpdateResponseDto> getReviewApi(
        @RequestAttribute("memberAltId") String memberAltId,
        @PathVariable("reviewId") String reviewAltId) {
        return ApiResponse.ok(reviewService.getReview(memberAltId, reviewAltId));
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<String> deleteReviewApi(@RequestAttribute("memberAltId") String memberAltId,
        @PathVariable("reviewId") String reviewAltId) {
        reviewService.deleteReview(memberAltId, reviewAltId);
        return ApiResponse.noContent();
    }

    @PutMapping("/{reviewId}")
    public ApiResponse<String> deleteReviewApi(@RequestAttribute("memberAltId") String memberAltId,
        @PathVariable("reviewId") String reviewAltId,
        @RequestPart("reviewUpdateDto") ReviewUpdateRequestDto reviewUpdateRequestDto,
        @RequestPart(value = "imageFiles", required = false) List<MultipartFile> multipartFiles) {
        reviewService.updateReview(memberAltId, reviewAltId, reviewUpdateRequestDto, multipartFiles);
        return ApiResponse.noContent();
    }
}
