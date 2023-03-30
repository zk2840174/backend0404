package org.zerock.backend.service;

import org.zerock.backend.dto.MemberDTO;

public interface MemberService {

    MemberDTO join (MemberDTO memberDTO);

    MemberDTO login(MemberDTO memberDTO);
}
