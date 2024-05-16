package com.refactoringhabit.member.domain.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CustomFileUtil customFileUtil;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("회원 가입 - 성공")
    @Test
    void memberJoin_Successfully() throws IOException {
        // given
        MemberJoinRequestDto memberJoinRequestDto = new MemberJoinRequestDto();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg",
            "image/jpeg", "test data".getBytes());
        when(customFileUtil.saveFile(file)).thenReturn("rename_profile.jpg");

        // when
        memberService.memberJoin(memberJoinRequestDto, file);

        //then
        verify(customFileUtil, times(1)).saveFile(file);
        verify(memberRepository, times(1)).save(any());
    }

    @DisplayName("회원 가입 - 실패 : 파일 저장 예외 발생")
    @Test
    void memberJoin_FileSaveFailed() throws IOException {
        // Given
        MemberJoinRequestDto memberJoinRequestDto = new MemberJoinRequestDto();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg",
            "image/jpeg", "test data".getBytes());
        when(customFileUtil.saveFile(file)).thenThrow(new IOException());

        // When, Then
        assertThrows(FileSaveFailedException.class,
            () -> memberService.memberJoin(memberJoinRequestDto, file));
        verify(customFileUtil, times(1)).saveFile(file);
        verify(memberRepository, never()).save(any());
    }
}
