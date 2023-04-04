package org.zerock.backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.backend.dto.MemberDTO;
import org.zerock.backend.service.SocialLoginService;
import org.zerock.backend.util.JWTUtil;
import org.zerock.backend.util.exceptions.CustomJWTException;


import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class KakaoLoginController {


    private final SocialLoginService socialService;



    @GetMapping("/kakao/login")
    public MemberDTO getKakaoUserEmail(String authCode){

        log.info("authCode.................." + authCode);


        return socialService.getKakaoEmail(authCode);

    }


}













