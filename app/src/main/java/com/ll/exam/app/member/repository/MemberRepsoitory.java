package com.ll.exam.app.member.repository;

import com.ll.exam.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepsoitory extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
}
