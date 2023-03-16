package org.zerock.backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.backend.dto.TokenDTO;
import org.zerock.backend.service.SocialLoginService;
import org.zerock.backend.util.JWTUtil;
import org.zerock.backend.util.exceptions.CustomJWTException;


import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class LoginController {


    private final SocialLoginService kakaoLoginService;


    private final JWTUtil jwtUtil;

    @GetMapping("/kakao/login/")
    public TokenDTO getKakaoUserEmail(String authCode){

        String email = kakaoLoginService.getKakaoEmail(authCode);

        log.info("-------------------------------");
        log.info(email);

        Map<String, Object> claimMap = Map.of("email", email);

        //String accessToken = jwtUtil.generateToken(claimMap, 10);

        String accessToken = jwtUtil.generateToken(claimMap, 1);


        String refreshToken = jwtUtil.generateToken(claimMap, 60*24*7);

        return TokenDTO.builder()
                .email(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @PostMapping("/refreshJWT")
    public Map<String, String> refreshJWT(@RequestBody TokenDTO refreshDTO) {

        try {
            //condition
            //refreshToken must not expire
            boolean expiredRefresh = checkExpiredToken(refreshDTO.getRefreshToken());

            log.info("expiredRefresh: " + expiredRefresh);

            //refresh token이 만료되었다면 그냥 새로 로그인을 요구
            if (expiredRefresh) {
                return Map.of("result", "Login");
            }

            //access Token expired
            boolean expiredAccess = checkExpiredToken(refreshDTO.getAccessToken());


            log.info("expiredAccess: " + expiredAccess);

            //access Token이 만료되지 않았다면
            if (!expiredAccess) {
                return Map.of("result", "not yet");
            }

            //refresh token은 남아있고 accessToken은 만료되어야 새로운 토큰 발행
            if (expiredAccess && !expiredRefresh) {

                Map<String, Object> claims = Map.of("email", refreshDTO.getEmail());

                return Map.of("result", "refreshed",
                        "email", refreshDTO.getEmail(),
                        "accessToken", jwtUtil.generateToken(claims, 1),
                        "refreshToken", jwtUtil.generateToken(claims, 60*24*7));

            }
        }catch(RuntimeException runtimeException){
            return Map.of("result", runtimeException.getMessage());
        }

        return null;
    }

    private boolean checkExpiredToken(String jwtStr)throws RuntimeException{

        if(jwtStr == null) {
            throw new RuntimeException("Null Token");
        }

        boolean expired = false;

        try{

            jwtUtil.validateToken(jwtStr);


        }catch(CustomJWTException customJWTException){

            log.info("checkExpiredToken ");
            log.info(customJWTException);

            if(customJWTException.getMessage().equals("Expired")) {
                expired = true;
            }
        }

        return expired;
    }
}













