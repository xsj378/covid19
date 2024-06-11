package com.cy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class IndexController {

    // 期望通过这个方法能找到 data.html
    @RequestMapping("/data")
    public  String toData(){
        return "data";
    }

    // 期望通过这个方法能找到 wz.html
    @RequestMapping("/wz")
    public  String toIndex(){
        return "wz";
    }

    // 期望通过这个方法能找到 yqqs.html
    @RequestMapping("/yqqs")
    public  String toYqqs(){
        return "yqqs";
    }
}
