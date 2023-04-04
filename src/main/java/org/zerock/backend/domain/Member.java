package org.zerock.backend.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_member")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    private String email;

    private String pw;

    private String accessToken;

    private String refreshToken;

    @Builder.Default
    private boolean social = false;

    public void changeAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

}
