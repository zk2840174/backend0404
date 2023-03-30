package org.zerock.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.backend.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {


    @Query("select m from Member m where m.email = :email and m.pw = :pw")
    Optional<Member> getMemberWithPw(String email, String pw);
}
