package com.refactoringhabit.wish.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wish")
public class WishController {

    @GetMapping("/list")
    public String wishList(@RequestAttribute("memberAltId") String memberAltId, Model model) {
        model.addAttribute("memberAltId", memberAltId);
        return "/pages/member/wish";
    }
}
