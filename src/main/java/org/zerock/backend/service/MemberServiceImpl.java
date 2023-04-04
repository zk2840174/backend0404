package org.zerock.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.backend.domain.Member;
import org.zerock.backend.dto.MemberDTO;
import org.zerock.backend.repository.MemberRepository;
import org.zerock.backend.util.JWTUtil;
import org.zerock.backend.util.PasswordUtil;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    private final JWTUtil jwtUtil;

    private final PasswordUtil passwordUtil;

    @Override
    public MemberDTO join(MemberDTO memberDTO) {

        //check email exists
        Optional<Member> result = memberRepository.findById(memberDTO.getEmail());

        Member member = result.get();

        if(member != null){
            throw new RuntimeException("Member Email Already Exist");
        }

        String pw = memberDTO.getPw();

        if(pw == null){
            pw = passwordUtil.generatePassword();
            memberDTO.setPw(pw);
        }

        //makeTokens
        makeTokens(memberDTO);


        //save to database
        Member memberData = modelMapper.map(memberDTO, Member.class);

        memberRepository.save(memberData);

        return memberDTO;
    }

    @Override
    public MemberDTO login(MemberDTO memberDTO) {

        Optional<Member> result = memberRepository.getMemberWithPw(memberDTO.getEmail(), memberDTO.getPw());

        Member member = result.orElseThrow();

        log.info("repository..... " + member);

        //generate new Tokens
        makeTokens(memberDTO);

        member.changeAccessToken(memberDTO.getAccessToken());
        member.changeRefreshToken(memberDTO.getRefreshToken());

        memberRepository.save(member);

        return memberDTO;
    }

    private void makeTokens(MemberDTO memberDTO){
        //makeTokens
        Map<String, Object> claims = Map.of("email", memberDTO.getEmail());
        //accessToken
        String accessToken = jwtUtil.generateToken(claims, 1);
        //refreshToken
        String refreshToken = jwtUtil.generateToken(claims, 60*24);

        memberDTO.setAccessToken(accessToken);
        memberDTO.setRefreshToken(refreshToken);

    }

}
