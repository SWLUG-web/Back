package com.boot.swlugweb.v1.apply;

import org.springframework.web.bind.annotation.*;

@RestController
public class ApplyController {

    @GetMapping("/apply")
    public Boolean apply() {

        return true;
    }

}
