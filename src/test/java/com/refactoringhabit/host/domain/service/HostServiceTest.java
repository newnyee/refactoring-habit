package com.refactoringhabit.host.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.HOST_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.member.domain.enums.MemberType.HOST;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.host.domain.repository.HostRepository;
import com.refactoringhabit.host.dto.HostInfoRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;


@ExtendWith(MockitoExtension.class)
class HostServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private HostRepository hostRepository;

    @Mock
    private CustomFileUtil customFileUtil;

    @Mock
    private Host host;

    @Mock
    private Member member;

    @Mock
    private HostInfoRequestDto hostInfoRequestDto;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private HostService hostService;


    @Test
    @DisplayName("호스트 가입 - 성공")
    void testHostJoin_Success() throws IOException {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), HOST))
            .thenReturn("getFileName");

        hostService.hostJoin(MEMBER_ALT_ID.getName(), hostInfoRequestDto, multipartFile);
        verify(member).setType(HOST);
    }

    @Test
    @DisplayName("호스트 가입 - 실패 : 찾을 수 없는 회원")
    void testHostJoin_NotFoundUser() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () ->
            hostService.hostJoin(MEMBER_ALT_ID.getName(), hostInfoRequestDto, multipartFile)
        );
    }

    @Test
    @DisplayName("호스트 가입 - 실패 : 파일 저장 실패")
    void testHostJoin_FileSaveFailed() throws IOException {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), HOST))
            .thenThrow(IOException.class);

        assertThrows(FileSaveFailedException.class, () ->
            hostService.hostJoin(MEMBER_ALT_ID.getName(), hostInfoRequestDto, multipartFile)
        );
        verify(member).setType(HOST);
    }

    @Test
    @DisplayName("닉네임 존재 확인 - 성공")
    void testNickNameCheck_Success() {
        when(hostRepository.existsByNickName("nickName")).thenReturn(false);

        assertFalse(hostService.nickNameCheck("nickName"));
    }

    @Test
    @DisplayName("호스트 정보 얻기 - 성공")
    void testGetHostInfo_Success() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(member.getHost()).thenReturn(host);

        hostService.getHostInfo(MEMBER_ALT_ID.getName());
    }

    @Test
    @DisplayName("호스트 정보 얻기 - 실패 : 찾을 수 없는 회원")
    void testGetHostInfo_NotFoundUser() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () ->
            hostService.getHostInfo(MEMBER_ALT_ID.getName())
        );
    }

    @Test
    @DisplayName("호스트 정보 수정 - 성공")
    void testHostInfoUpdate_Success() throws IOException {
        when(hostRepository.findByAltId(HOST_ALT_ID.getName())).thenReturn(Optional.of(host));
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), HOST))
            .thenReturn("getFileName");

        hostService.hostInfoUpdate(HOST_ALT_ID.getName(), hostInfoRequestDto, multipartFile);
    }

    @Test
    @DisplayName("호스트 정보 수정 - 실패 : 찾을 수 없는 회원")
    void testHostInfoUpdate_NotFoundUser() {
        when(hostRepository.findByAltId(HOST_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () ->
            hostService.hostInfoUpdate(HOST_ALT_ID.getName(), hostInfoRequestDto, multipartFile)
        );
    }

    @Test
    @DisplayName("호스트 정보 수정 - 실패 : 파일 저장 실패")
    void testHostInfoUpdate_FileSaveFailed() throws IOException {
        when(hostRepository.findByAltId(HOST_ALT_ID.getName())).thenReturn(Optional.of(host));
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), HOST))
            .thenThrow(IOException.class);

        assertThrows(FileSaveFailedException.class, () ->
            hostService.hostInfoUpdate(HOST_ALT_ID.getName(), hostInfoRequestDto, multipartFile)
        );
    }
}
