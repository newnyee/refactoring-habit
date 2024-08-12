package com.refactoringhabit.product.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.PRODUCT_ALT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.common.domain.repository.RedisRepository;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.dto.ProductDetailDto;
import com.refactoringhabit.product.dto.SimpleProductInfoDto;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private List<ProductCardDto> productDtos;

    @InjectMocks
    private ProductService productService;

    private static final String CACHE_NAME_POPULAR_PRODUCT = "popular-products";
    private static final String CACHE_NAME_NEW_PRODUCT = "new-products";
    private static final String CACHE_KEY_NAME = "SimpleKey []";
    private static final String CATEGORY_NAME = "categoryName";
    private static final String ORDER_BY_VALUE = "orderByValue";

    @Test
    @DisplayName("인기 상품 캐시 업데이트")
    void testUpdatePopularProductsCache() {
        when(productRepository.productsOrderBySalesVolumeAndReviewAverage())
            .thenReturn(productDtos);

        productService.updatePopularProductsCache();
        verify(redisRepository)
            .setCache(CACHE_NAME_POPULAR_PRODUCT, CACHE_KEY_NAME, productDtos);
    }

    @Test
    @DisplayName("신규 상품 캐시 업데이트")
    void testUpdateNewProductsCache() {
        when(productRepository.productsOrderByCreatedAt()).thenReturn(productDtos);

        productService.updateNewProductsCache();
        verify(redisRepository)
            .setCache(CACHE_NAME_NEW_PRODUCT, CACHE_KEY_NAME, productDtos);
    }

    @Test
    @DisplayName("대분류에 따른 상품 리스트 얻기")
    void testGetProductByCategoryLarge() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByCategoryName(CATEGORY_NAME, pageable, ORDER_BY_VALUE))
            .thenReturn(Collections.emptyList());

        List<ProductCardDto> products =
            productService.getProductsByCategoryLarge(CATEGORY_NAME, pageable, ORDER_BY_VALUE);
        assertEquals(Collections.emptyList(), products);
    }

    @Test
    @DisplayName("대분류에 따른 상품 갯수 얻기")
    void testGetProductCountByCategoryLarge() {
        when(productRepository.countByCategoryName(CATEGORY_NAME))
            .thenReturn(0L);

        Long count = productService.getProductCountByCategoryLarge(CATEGORY_NAME);
        assertEquals(0L, count);
    }

    @Test
    @DisplayName("상품 아이디에 따른 상품 상세 정보 얻기")
    void testGetProductDetailsById() {
        ProductDetailDto productDetailDto = mock(ProductDetailDto.class);
        when(productRepository.getProductDetailsByAltId(PRODUCT_ALT_ID.getName()))
            .thenReturn(productDetailDto);

        ProductDetailDto getproductDetailDto =
            productService.getProductDetailsById(PRODUCT_ALT_ID.getName());
        assertEquals(productDetailDto, getproductDetailDto);
    }

    @Test
    @DisplayName("상품 아이디에 따른 간단 상품 정보 얻기")
    void testGetSimpleProductInfo() {
        SimpleProductInfoDto productInfoDto = mock(SimpleProductInfoDto.class);
        when(productRepository.getSimpleProductInfoByAltId(PRODUCT_ALT_ID.getName()))
            .thenReturn(productInfoDto);

        SimpleProductInfoDto getSimpleProductInfo = productService.getSimpleProductInfo(
            PRODUCT_ALT_ID.getName());
        assertEquals(productInfoDto, getSimpleProductInfo);
    }
}
