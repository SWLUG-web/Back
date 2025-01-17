package com.boot.swlugweb.v1.apply;

import org.springframework.web.bind.annotation.*;

//회원가입
@RestController
@RequestMapping("/api/apply")
public class ApplyController {

    @GetMapping
    public Boolean apply() {

        return true;
    }

}
