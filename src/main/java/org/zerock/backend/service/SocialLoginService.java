package org.zerock.backend.service;

import org.zerock.backend.dto.MemberDTO;

public interface SocialLoginService {

    MemberDTO getKakaoEmail(String authCode);
}
