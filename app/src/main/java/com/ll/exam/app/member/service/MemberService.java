package com.ll.exam.app.member.service;

import com.ll.exam.app.member.entity.Member;
import com.ll.exam.app.member.repository.MemberRepsoitory;

import com.ll.exam.app.util.Util;
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
    private final MemberRepsoitory memberRepository;

    public Member getMemberByUsername(String username) {
        return memberRepsoitory.findByUsername(username).orElse(null);
    }

    private String getCurrentProfileImgDirName() {
        return "member/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");
    }

    public Member join(String username, String password, String email, MultipartFile profileImg) {
        String profileImgDirName = getCurrentProfileImgDirName();

        String ext  = Util.file.getExt(profileImg.getOriginalFilename());

        String fileName = UUID.randomUUID() + "." + ext;
        String profileImgDirPath = genFileDirPath + "/" + profileImgDirName;
        String profileImgFilePath = profileImgDirPath + "/" + fileName;

        new File(profileImgDirPath).mkdirs();

        try {
            profileImg.transferTo(new File(profileImgFilePath));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        String profileImgRelPath = profileImgDirName + "/" + fileName;

        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .profileImg(profileImgRelPath)
                .build();
        memberRepsoitory.save(member);

        return member;
    }

    public Member getMemberById(Long id) {
        return memberRepsoitory.findById(id).orElse(null);
    }



    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
        memberRepsoitory.save(member);

        return member;
    }

    public long count() {
        return memberRepsoitory.count();
    }

    public void removeProfileImg(Member member) {
        member.removeProfileImgOnStorage(); // 파일 삭제
        member.setProfileImg(null);
        memberRepsoitory.save(member);
    }

    public void setProfileImgByUrl(Member member, String url) {
        String filePath = Util.file.downloading(url, genFileDirPath + "/" +getCurrentProfileImgDirName() + "/" + UUID.randomUUID());
        member.setProfileImg(getCurrentProfileImgDirName() + "/" + new File(filePath).getName());
        memberRepsoitory.save(member);
    }
}
