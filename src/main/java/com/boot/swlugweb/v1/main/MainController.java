package com.boot.swlugweb.v1.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value =  {"/", "/main"})
public class MainController {

    @GetMapping
    public String mainPage() {
        return "Hello World";
    }
    
}
