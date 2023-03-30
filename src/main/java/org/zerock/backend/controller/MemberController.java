package org.zerock.backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.backend.dto.MemberDTO;
import org.zerock.backend.service.MemberService;

@RestController
@RequestMapping("/members")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public MemberDTO join( @RequestBody MemberDTO memberDTO){

        MemberDTO resultDTO = memberService.join(memberDTO);

        log.info("JOIN RESULT: " + resultDTO);

        return resultDTO;
    }

    @PostMapping("/login")
    public MemberDTO login( @RequestBody MemberDTO memberDTO){

        MemberDTO resultDTO = memberService.login(memberDTO);

        log.info("LOGIN RESULT: " + resultDTO);

        return resultDTO;
    }


}
