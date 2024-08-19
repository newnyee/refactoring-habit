package com.refactoringhabit.product.domain.service;

import com.refactoringhabit.common.domain.repository.RedisRepository;
import com.refactoringhabit.common.exception.CustomException;
import com.refactoringhabit.host.domain.repository.HostRepository;
import com.refactoringhabit.host.dto.SimpleHostInfoDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.domain.exception.NotFoundProductException;
import com.refactoringhabit.product.domain.mapper.ProductEntityMapper;
import com.refactoringhabit.product.domain.repository.OptionRepository;
import com.refactoringhabit.product.dto.OptionDetailDto;
import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.product.dto.ProductDetailDto;
import com.refactoringhabit.product.dto.ProductResponseDto;
import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import com.refactoringhabit.wish.domain.exception.NotFoundWishException;
import com.refactoringhabit.wish.domain.repository.WishRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisRepository redisRepository;
    private final OptionRepository optionRepository;
    private final HostRepository hostRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;

    private static final String CACHE_NAME_POPULAR_PRODUCT = "popular-products";
    private static final String CACHE_NAME_NEW_PRODUCT = "new-products";
    private static final String CACHE_KEY_NAME = "SimpleKey []";
    private static final int REVIEW_PAGE_SIZE = 8;
    private static final String ORDER_BY_VALUE = "createAt";

    @Cacheable(value = CACHE_NAME_POPULAR_PRODUCT, cacheManager = "redisCacheManager")
    public List<ProductCardDto> getPopularProducts() {
        return productRepository.productsOrderBySalesVolumeAndReviewAverage();
    }

    @Cacheable(value = CACHE_NAME_NEW_PRODUCT, cacheManager = "redisCacheManager")
    public List<ProductCardDto> getNewProducts() {
        return productRepository.productsOrderByCreatedAt();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updatePopularProductsCache() {
        log.info("Updating popular products cache ...");
        List<ProductCardDto> popularProducts =
            productRepository.productsOrderBySalesVolumeAndReviewAverage();
        redisRepository.setCache(CACHE_NAME_POPULAR_PRODUCT, CACHE_KEY_NAME, popularProducts);
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void updateNewProductsCache() {
        log.info("Updating new products cache ...");
        List<ProductCardDto> newProducts =
            productRepository.productsOrderByCreatedAt();
        redisRepository.setCache(CACHE_NAME_NEW_PRODUCT, CACHE_KEY_NAME, newProducts);
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getProductsByCategoryLarge(String categoryName, Pageable pageable,
        String orderByValue) {
        return productRepository.findByCategoryName(categoryName, pageable, orderByValue);
    }

    @Transactional(readOnly = true)
    public Long getProductCountByCategoryLarge(String categoryName) {
        return productRepository.countByCategoryName(categoryName);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductDetailsById(String memberAltId, String productAltId) {
        ProductDetailDto productDetailDto =
            productRepository.getProductDetailsByAltId(productAltId);

        List<OptionDetailDto> optionDetailDtos = optionRepository.getOptionDetailDtos(productAltId);

        SimpleHostInfoDto simpleHostInfoDto =
            hostRepository.getSimpleHostInfoById(productDetailDto.hostId());

        Pageable pageable = Pageable.ofSize(REVIEW_PAGE_SIZE);
        List<ReviewDetailDto> reviewDetailDtos =
            reviewRepository.findByProductIdLimit(productAltId, pageable, ORDER_BY_VALUE);

        String wishAltId = getWishAltId(memberAltId, productAltId);

        return ProductEntityMapper.INSTANCE
            .toProductResponseDto(productDetailDto, optionDetailDtos,
                simpleHostInfoDto, reviewDetailDtos, wishAltId);
    }

    private String getWishAltId(String memberAltId, String productAltId) {
        try {
            return wishRepository
                .findAltIdByMemberAndProduct(getMember(memberAltId), getProduct(productAltId))
                .orElseThrow(NotFoundWishException::new);
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
            .orElseThrow(NotFoundProductException::new);
    }
}
