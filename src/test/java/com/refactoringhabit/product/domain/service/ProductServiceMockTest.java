package com.refactoringhabit.product.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.PRODUCT_ALT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.common.domain.repository.RedisRepository;
import com.refactoringhabit.host.domain.repository.HostRepository;
import com.refactoringhabit.host.dto.SimpleHostInfoDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.domain.repository.OptionRepository;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.product.dto.OptionDetailDto;
import com.refactoringhabit.product.dto.ProductCardDto;
import com.refactoringhabit.product.dto.ProductDetailDto;
import com.refactoringhabit.product.dto.ProductResponseDto;
import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewDetailDto;
import com.refactoringhabit.wish.domain.exception.NotFoundWishException;
import com.refactoringhabit.wish.domain.repository.WishRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private HostRepository hostRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private List<ProductCardDto> productDtos;

    private static final String CACHE_NAME_POPULAR_PRODUCT = "popular-products";
    private static final String CACHE_NAME_NEW_PRODUCT = "new-products";
    private static final String CACHE_KEY_NAME = "SimpleKey []";
    private static final String CATEGORY_NAME = "categoryName";
    private static final String ORDER_BY_VALUE = "orderByValue";
    private static final String WISH_ALT_ID = "wishAltId";
    private static final Long HOST_ID = 1L;

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
    @DisplayName("상품 아이디에따른 상품 상세 정보 얻기 - 찜이 있는경우")
    void testGetProductDetailsById_ExistsWish() {
        ProductDetailDto productDetailDto = mock(ProductDetailDto.class);
        OptionDetailDto optionDetailDto = mock(OptionDetailDto.class);
        List<OptionDetailDto> optionDetailDtos = Collections.singletonList(optionDetailDto);
        SimpleHostInfoDto simpleHostInfoDto = mock(SimpleHostInfoDto.class);
        ReviewDetailDto reviewDetailDto = mock(ReviewDetailDto.class);
        List<ReviewDetailDto> reviewDetailDtos  = Collections.singletonList(reviewDetailDto);
        Member member = mock(Member.class);
        Product product = mock(Product.class);

        when(productRepository.getProductDetailsByAltId(PRODUCT_ALT_ID.getName())).thenReturn(productDetailDto);
        when(optionRepository.getOptionDetailDtos(PRODUCT_ALT_ID.getName())).thenReturn(optionDetailDtos);
        when(productDetailDto.hostId()).thenReturn(HOST_ID);
        when(hostRepository.getSimpleHostInfoById(HOST_ID)).thenReturn(simpleHostInfoDto);
        when(reviewRepository.findByProductIdLimit(any(), any(), any())).thenReturn(reviewDetailDtos);
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName())).thenReturn(Optional.of(member));
        when(productRepository.findByAltId(PRODUCT_ALT_ID.getName())).thenReturn(Optional.of(product));
        when(wishRepository.findAltIdByMemberAndProduct(member, product)).thenReturn(Optional.of(WISH_ALT_ID));

        ProductResponseDto getProductDetailsById = productService
            .getProductDetailsById(MEMBER_ALT_ID.getName(), PRODUCT_ALT_ID.getName());
        assertEquals(WISH_ALT_ID, getProductDetailsById.wishAltId());
    }

    @Test
    @DisplayName("상품 아이디에따른 상품 상세 정보 얻기 - 찜이 없는경우")
    void testGetProductDetailsById_NotExistsWish() {
        ProductDetailDto productDetailDto = mock(ProductDetailDto.class);
        OptionDetailDto optionDetailDto = mock(OptionDetailDto.class);
        List<OptionDetailDto> optionDetailDtos = Collections.singletonList(optionDetailDto);
        SimpleHostInfoDto simpleHostInfoDto = mock(SimpleHostInfoDto.class);
        ReviewDetailDto reviewDetailDto = mock(ReviewDetailDto.class);
        List<ReviewDetailDto> reviewDetailDtos  = Collections.singletonList(reviewDetailDto);
        Member member = mock(Member.class);
        Product product = mock(Product.class);

        when(productRepository.getProductDetailsByAltId(PRODUCT_ALT_ID.getName()))
            .thenReturn(productDetailDto);
        when(optionRepository.getOptionDetailDtos(PRODUCT_ALT_ID.getName()))
            .thenReturn(optionDetailDtos);
        when(productDetailDto.hostId()).thenReturn(HOST_ID);
        when(hostRepository.getSimpleHostInfoById(HOST_ID)).thenReturn(simpleHostInfoDto);
        when(reviewRepository.findByProductIdLimit(any(), any(), any()))
            .thenReturn(reviewDetailDtos);
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(productRepository.findByAltId(PRODUCT_ALT_ID.getName()))
            .thenReturn(Optional.of(product));
        when(wishRepository.findAltIdByMemberAndProduct(member, product))
            .thenThrow(NotFoundWishException.class);

        ProductResponseDto getProductDetailsById = productService
            .getProductDetailsById(MEMBER_ALT_ID.getName(), PRODUCT_ALT_ID.getName());
        assertEquals("", getProductDetailsById.wishAltId());
    }
}
