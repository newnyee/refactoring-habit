package com.refactoringhabit.category.controller;

import com.refactoringhabit.category.domain.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        return "/pages/product/category-list";
    }
}
