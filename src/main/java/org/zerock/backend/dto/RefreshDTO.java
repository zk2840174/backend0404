package org.zerock.backend.dto;


import lombok.Data;

@Data
public class RefreshDTO {

    private String email;

    private String accessToken;

    private String refreshToken;
}
