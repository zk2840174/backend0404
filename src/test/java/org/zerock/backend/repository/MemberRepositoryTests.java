package org.zerock.backend.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.backend.domain.Member;
import org.zerock.backend.util.JWTUtil;

import java.util.Map;


@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testInserts(){

        for(int i = 10; i < 20; i++ ){

            String email = "user"+i+"@aaa.com";

            Map<String, Object> valueMap = Map.of("email", email);

            Member member = Member.builder()
                    .email(email)
                    .pw("1111")
                    .accessToken(jwtUtil.generateToken(valueMap, 10))
                    .refreshToken(jwtUtil.generateToken(valueMap, 60* 24))
                    .build();

            memberRepository.save(member);


        }


    }
}
