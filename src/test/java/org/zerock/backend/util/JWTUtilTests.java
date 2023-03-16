package org.zerock.backend.util;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testGenerate() {

        Map<String, Object> claimMap = Map.of("email", "testuser@aaa.com");

        String jwtStr = jwtUtil.generateToken(claimMap, 60);

        log.info(jwtStr);
    }

    @Test
    public void testValidate() {

        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3R1c2VyQGFhYS5jb20iLCJpYXQiOjE2Nzg3OTYxOTgsImV4cCI6MTY3ODc5Njc5OH0.boLlcoHcqc87vr39YTRlfKzhI7RXuFTAHFZl3A7rcqA";

        Map<String, Object>  claims = jwtUtil.validateToken(jwtStr);


        log.info("-------------------------");
        log.info(claims);
    }
}
