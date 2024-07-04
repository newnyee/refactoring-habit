package com.refactoringhabit.member.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.member.domain.enums.MemberType.MEMBER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import com.refactoringhabit.member.dto.MemberUpdateInfoRequestDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CustomFileUtil customFileUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Member member;

    @Mock
    private MemberJoinRequestDto memberJoinRequestDto;

    @Mock
    private MemberUpdateInfoRequestDto memberUpdateInfoRequestDto;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private MemberService memberService;

    private static final String PASSWORD = "password";


    @DisplayName("회원 가입 - 성공")
    @Test
    void memberJoin_Successfully() {
        when(passwordEncoder.encode(memberJoinRequestDto.getPassword()))
            .thenReturn("encodedPassword");
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), MEMBER))
            .thenReturn("rename_profile.jpg");

        memberService.memberJoin(memberJoinRequestDto, multipartFile);
        verify(memberJoinRequestDto).setEncodedPassword("encodedPassword");
        verify(memberJoinRequestDto).setProfileImage("rename_profile.jpg");
        verify(memberRepository).save(any());
    }

    @DisplayName("회원 가입 - 실패 : 파일 저장 예외 발생")
    @Test
    void memberJoin_FileSaveFailed() {
        when(passwordEncoder.encode(memberJoinRequestDto.getPassword()))
            .thenReturn("encodedPassword");
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), MEMBER))
            .thenThrow(new FileSaveFailedException());

        assertThrows(FileSaveFailedException.class, () ->
            memberService.memberJoin(memberJoinRequestDto, multipartFile));
        verify(memberJoinRequestDto).setEncodedPassword("encodedPassword");
        verify(memberRepository, never()).save(any());
    }

    @DisplayName("회원 정보 변경 - 성공 : 이미지 파일 존재")
    @Test
    void testMemberUpdate_Success_ExistImage() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(memberUpdateInfoRequestDto.getPassword()).thenReturn(null);
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), MEMBER))
            .thenReturn("image.png");

        memberService
            .memberUpdate(MEMBER_ALT_ID.getName(), memberUpdateInfoRequestDto, multipartFile);
    }

    @DisplayName("회원 정보 변경 - 성공 : 이미지 파일 존재하지 않음")
    @Test
    void testMemberUpdate_Success_NotExistImage() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(memberUpdateInfoRequestDto.getPassword()).thenReturn(null);

        memberService
            .memberUpdate(MEMBER_ALT_ID.getName(), memberUpdateInfoRequestDto, null);
    }

    @DisplayName("회원 정보 변경 - 실패 : 파일 저장 실패")
    @Test
    void testMemberUpdate() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(memberUpdateInfoRequestDto.getPassword()).thenReturn(null);
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), MEMBER))
            .thenThrow(FileSaveFailedException.class);

        assertThrows(FileSaveFailedException.class, () ->
            memberService
                .memberUpdate(MEMBER_ALT_ID.getName(), memberUpdateInfoRequestDto, multipartFile)
        );
    }

    @DisplayName("비밀번호 변경 - 성공")
    @Test
    void testPasswordUpdate() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(memberUpdateInfoRequestDto.getPassword()).thenReturn(PASSWORD);

        memberService.memberUpdate(MEMBER_ALT_ID.getName(), memberUpdateInfoRequestDto, null);
        verify(passwordEncoder).encode(PASSWORD);
    }

    @DisplayName("회원 정보 찾기 - 성공")
    @Test
    void testGetMemberInfo_Success() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));

        memberService.getMemberInfo(MEMBER_ALT_ID.getName());
    }

    @DisplayName("회원 정보 찾기 - 실패 : 찾을 수 없는 사용자")
    @Test
    void testGetMemberInfo_UserNotFound() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () ->
            memberService.getMemberInfo(MEMBER_ALT_ID.getName())
        );
    }
}
