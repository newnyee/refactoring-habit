package com.refactoringhabit.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @GetMapping
    public String showCart(@RequestAttribute("memberAltId") String memberAltId, Model model) {
        model.addAttribute("memberAltId", memberAltId);
        return "/pages/member/cart";
    }
}
