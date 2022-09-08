package com.vpcons.nsereportsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class IndexController {

    @GetMapping({ "/", "/index","eod"})
    public String eod(Model model) {
        return "eod";
    }

    @GetMapping({ "cash"})
    public String cash(Model model) {
        return "cash";
    }
}
