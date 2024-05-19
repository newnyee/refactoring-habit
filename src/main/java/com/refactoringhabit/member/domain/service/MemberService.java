package com.refactoringhabit.member.domain.service;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import com.refactoringhabit.member.domain.mapper.MemberEntityMapper;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomFileUtil customFileUtil;
    private final PasswordEncoder passwordEncoder;

    public static final String MID = null;

    @Transactional
    public void memberJoin(
        MemberJoinRequestDto memberJoinRequestDto, MultipartFile multipartFile) {

        try {
            memberJoinRequestDto.setEncodedPassword(
                passwordEncoder.encode(memberJoinRequestDto.getPassword()));
            memberJoinRequestDto.setProfileImage(customFileUtil.saveFile(multipartFile));

            memberRepository
                .save(MemberEntityMapper.INSTANCE.toEntity(memberJoinRequestDto, MID));
        } catch (IOException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileSaveFailedException();
        }
    }
}
