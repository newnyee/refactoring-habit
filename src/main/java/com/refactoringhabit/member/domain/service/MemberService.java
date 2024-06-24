package com.refactoringhabit.member.domain.service;

import static com.refactoringhabit.member.domain.enums.MemberType.MEMBER;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.mapper.MemberEntityMapper;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.member.dto.MemberInfoResponseDto;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import com.refactoringhabit.member.dto.MemberUpdateInfoRequestDto;
import java.io.IOException;
import java.util.Optional;
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

    public static final String MEMBER_ALT_ID = null;

    @Transactional
    public void memberJoin(
        MemberJoinRequestDto memberJoinRequestDto, MultipartFile multipartFile) {

        try {
            memberJoinRequestDto.setEncodedPassword(
                passwordEncoder.encode(memberJoinRequestDto.getPassword()));
            memberJoinRequestDto.setProfileImage(
                customFileUtil.saveProfileImage(Optional.ofNullable(multipartFile), MEMBER));

            memberRepository
                .save(MemberEntityMapper.INSTANCE.toEntity(memberJoinRequestDto, MEMBER_ALT_ID));
        } catch (IOException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileSaveFailedException();
        }
    }

    @Transactional
    public void memberUpdate(
        String memberAltId, MemberUpdateInfoRequestDto memberUpdateInfoRequestDto,
        MultipartFile multipartFile) {

        Member member = memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);

        try {
            String encodedPassword =
                memberUpdateInfoRequestDto.getPassword() != null
                    ? passwordEncoder.encode(memberUpdateInfoRequestDto.getPassword())
                    : null;

            String profileImage =
                multipartFile != null
                    ? customFileUtil.saveProfileImage(Optional.of(multipartFile), MEMBER)
                    : null;

            MemberEntityMapper.INSTANCE.updateEntityFromDto(
                    memberUpdateInfoRequestDto, encodedPassword, profileImage, member);

        } catch (IOException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileSaveFailedException();
        }
    }

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(String memberAltId) {
        return MemberEntityMapper.INSTANCE.toMemberInfoResponseDto(
            memberRepository.findByAltId(memberAltId).orElseThrow(UserNotFoundException::new));
    }
}
