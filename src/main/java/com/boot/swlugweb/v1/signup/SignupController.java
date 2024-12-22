package com.boot.swlugweb.v1.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/signup")
public class SignupController {

    @Autowired
    private SignupService signupService;

    //회원가입 진행 -> 받는 파라미터로 이메일 인증 번호도 같이 받아야 함
    @PostMapping("/signup") //사용자가 작성한 폼을 받아옴
    public String register(@RequestBody SignupRequestDto signuprequestdto) {
        System.out.println("User ID: " + signuprequestdto.getUser_id());
        System.out.println("Email: " + signuprequestdto.getEmail());
        System.out.println("Phone: " + signuprequestdto.getPhone());
        signupService.registerUser(signuprequestdto);

        //로그인 페이지로 리다이렉트
        return "/api/v1/login" ;

    }

}
