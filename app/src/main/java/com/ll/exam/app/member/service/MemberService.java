package com.ll.exam.app.member.service;

import com.ll.exam.app.member.entity.Member;
import com.ll.exam.app.member.repository.MemberRepsoitory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;
    private final MemberRepsoitory memberRepsoitory;
    public Member getMemberByUsername(String username) {
        return memberRepsoitory.findByUsername(username).orElse(null);
    }

    public Member join(String username, String password, String email, MultipartFile profileImg) {
        String profileImgRelPath = "member/" + UUID.randomUUID().toString() + ".png";
        File profileImgFile = new File(genFileDirPath + "/" + profileImgRelPath);

        profileImgFile.mkdirs();

        try {
            profileImg.transferTo(profileImgFile);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .profileImg(profileImgRelPath)
                .build();
        memberRepsoitory.save(member);

        return member;
    }
}
