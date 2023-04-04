package org.zerock.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.backend.domain.Member;
import org.zerock.backend.dto.MemberDTO;
import org.zerock.backend.repository.MemberRepository;
import org.zerock.backend.util.JWTUtil;
import org.zerock.backend.util.PasswordUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class SocialLoginServiceImpl implements SocialLoginService {

    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    private final PasswordUtil passwordUtil;

    private final JWTUtil jwtUtil;

    @Value("${org.zerock.kakao.token_url}")
    private String TOKENURL;
    @Value("${org.zerock.kakao.rest_key}")
    private String CLIENT_ID;

    @Value("${org.zerock.kakao.redirect_uri}")
    private String REDIRECT_URI;

    @Value("${org.zerock.kakao.get_user_url}")
    private String GETUSER_URI;




    @Override
    public MemberDTO getKakaoEmail(String authCode) {

        String kakaoAccessToken = getAccessToken(authCode);

        log.info("--------------------------------1");
        log.info("kakao access token: " + kakaoAccessToken);

        String email = getEmailFromAccessToken(kakaoAccessToken);

        MemberDTO memberDTO = checkMemberDatabase(email);

        log.info("USER EMAIL: " + email);

        return memberDTO;
    }

    private MemberDTO checkMemberDatabase(String email) {

        log.info("===================================");
        log.info(email);

        Optional<Member> result = memberRepository.findById(email);

        Map<String, Object> valueMap = Map.of("email", email);

        String accessToken = jwtUtil.generateToken(valueMap, 1 );

        String refreshToken = jwtUtil.generateToken(valueMap, 60* 24);


        if(result.isPresent()){

            Member member = result.get();
            member.changeAccessToken(accessToken);
            member.changeRefreshToken(refreshToken);

            memberRepository.save(member);

            return modelMapper.map(member, MemberDTO.class);
        }

        //not exists email
        Member newMember = Member.builder()
                .email(email)
                .pw(passwordUtil.generatePassword())
                .accessToken(jwtUtil.generateToken(valueMap, 1 ))
                .refreshToken(jwtUtil.generateToken(valueMap, 60* 24))
                .social(true)
                .build();

        memberRepository.save(newMember);

        return modelMapper.map(newMember, MemberDTO.class);
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
