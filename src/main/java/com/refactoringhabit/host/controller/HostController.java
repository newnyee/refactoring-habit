package com.refactoringhabit.host.controller;

import com.refactoringhabit.host.domain.service.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/host")
public class HostController {

    private final HostService hostService;

    @GetMapping("/join")
    public String hostJoin() {
        return "/pages/host/host-join";
    }

    @GetMapping("/info")
    public String hostInfo(@RequestAttribute("memberAltId") String memberAltId, Model model) {
        model.addAttribute("updateHostInfo", hostService.getHostInfo(memberAltId));
        return "/pages/host/host-info-edit";
    }
}
