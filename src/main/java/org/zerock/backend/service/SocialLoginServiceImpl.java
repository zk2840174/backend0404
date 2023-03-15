package org.zerock.backend.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;


@Service
@Log4j2
public class SocialLoginServiceImpl implements SocialLoginService {

    @Value("${org.zerock.kakao.token_url}")
    private String TOKENURL;
    @Value("${org.zerock.kakao.rest_key}")
    private String CLIENT_ID;

    @Value("${org.zerock.kakao.redirect_uri}")
    private String REDIRECT_URI;

    @Value("${org.zerock.kakao.get_user_url}")
    private String GETUSER_URI;

    @Override
    public String getKakaoEmail(String authCode) {


        String accessToken = getAccessToken(authCode);

        log.info("--------------------------------1");
        log.info("access token: " + accessToken);

        String email = getEmailFromAccessToken(accessToken);

        log.info("USER EMAIL: " + email);

        return email;
    }

    private String getAccessToken(String authCode){

        String accessToken = null;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);


        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(TOKENURL)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri",REDIRECT_URI)
                .queryParam("code", authCode)
                .build(true);

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriBuilder.toString(), HttpMethod.POST, entity, LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, String> bodyMap = response.getBody();

        accessToken = bodyMap.get("access_token");

        return accessToken;

    }

    private String getEmailFromAccessToken(String accessToken){

        if(accessToken == null){
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type","application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(GETUSER_URI).build();

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("------------------------------------");
        log.info(bodyMap);

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");

        log.info("kakaoAccount: " + kakaoAccount);

        return kakaoAccount.get("email");

    }

}
