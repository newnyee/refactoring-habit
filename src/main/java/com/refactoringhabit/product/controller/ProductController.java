package com.refactoringhabit.product.controller;

import com.refactoringhabit.product.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/popular-list")
    public String popularProducts(Model model) {
        model.addAttribute("pageTitle", "인기 만점 해빗");
        model.addAttribute("productList", productService.getPopularProducts());
        return "/pages/home-product-list";
    }

    @GetMapping("/new-list")
    public String newProducts(Model model) {
        model.addAttribute("pageTitle", "신규 해빗");
        model.addAttribute("productList", productService.getNewProducts());
        return "/pages/home-product-list";
    }

    @GetMapping("/{productId}")
    public String productDetail(@PathVariable("productId") String productAltId, Model model) {
        model.addAttribute("productId", productAltId);
        return "/pages/product/product-detail";
    }

    @GetMapping("/{productId}/review")
    public String review(@PathVariable("productId") String productAltId, Model model) {
        model.addAttribute("productId", productAltId);
        return "/pages/product/product-review-list";
    }
}
