package com.refactoringhabit.host.domain.service;

import static com.refactoringhabit.common.enums.AttributeNames.HOST_ALT_ID;
import static com.refactoringhabit.common.enums.AttributeNames.MEMBER_ALT_ID;
import static com.refactoringhabit.member.domain.enums.MemberType.HOST;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.refactoringhabit.category.domain.entity.CategoryMiddle;
import com.refactoringhabit.category.domain.exception.CategoryNotFoundException;
import com.refactoringhabit.category.domain.repository.CategoryMiddleRepository;
import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.host.domain.repository.HostRepository;
import com.refactoringhabit.host.dto.HostInfoRequestDto;
import com.refactoringhabit.host.dto.HostOptionInfoDto;
import com.refactoringhabit.host.dto.HostProductInfoDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import java.util.List;
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
    private CategoryMiddleRepository categoryMiddleRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomFileUtil customFileUtil;

    @Mock
    private Host host;

    @Mock
    private Member member;

    @Mock
    private CategoryMiddle categoryMiddle;

    @Mock
    private Product product;

    @Mock
    private HostInfoRequestDto hostInfoRequestDto;

    @Mock
    private HostProductInfoDto hostProductInfoDto;

    @Mock
    private List<HostOptionInfoDto> hostOptionInfoDtoList;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private List<MultipartFile> multipartFiles;

    @InjectMocks
    private HostService hostService;

    private final static String CATEGORY_MIDDLE_ALT_ID = "categoryMiddleAltId";


    @Test
    @DisplayName("호스트 가입 - 성공")
    void testHostJoin_Success() {
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
    void testHostJoin_FileSaveFailed() {
        when(memberRepository.findByAltId(MEMBER_ALT_ID.getName()))
            .thenReturn(Optional.of(member));
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), HOST))
            .thenThrow(FileSaveFailedException.class);

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
    void testHostInfoUpdate_Success() {
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
    void testHostInfoUpdate_FileSaveFailed() {
        when(hostRepository.findByAltId(HOST_ALT_ID.getName())).thenReturn(Optional.of(host));
        when(customFileUtil.saveProfileImage(Optional.of(multipartFile), HOST))
            .thenThrow(FileSaveFailedException.class);

        assertThrows(FileSaveFailedException.class, () ->
            hostService.hostInfoUpdate(HOST_ALT_ID.getName(), hostInfoRequestDto, multipartFile)
        );
    }

    @Test
    @DisplayName("호스트 상품 등록 - 성공")
    void testHostProductCreate_Success() {
        when(hostProductInfoDto.getCategoryMiddleAltId()).thenReturn(CATEGORY_MIDDLE_ALT_ID);
        when(categoryMiddleRepository.findByAltId(CATEGORY_MIDDLE_ALT_ID))
            .thenReturn(Optional.of(categoryMiddle));
        when(customFileUtil.saveImageFiles(multipartFiles)).thenReturn("imageFileNames");
        when(hostRepository.findByAltId(HOST_ALT_ID.getName())).thenReturn(Optional.of(host));
        when(productRepository.save(any())).thenReturn(product);
        when(hostProductInfoDto.getOptionInfoList()).thenReturn(hostOptionInfoDtoList);

        hostService.hostProductCreate(HOST_ALT_ID.getName(), hostProductInfoDto, multipartFiles);
    }

    @Test
    @DisplayName("호스트 상품 등록 - 실패 : 찾을 수 없는 카테고리")
    void testHostProductCreate_CategoryNotFound() {
        when(hostProductInfoDto.getCategoryMiddleAltId())
            .thenThrow(CategoryNotFoundException.class);

        assertThrows(CategoryNotFoundException.class, () ->
            hostService
                .hostProductCreate(HOST_ALT_ID.getName(), hostProductInfoDto, multipartFiles)
        );
    }

    @Test
    @DisplayName("호스트 상품 등록 - 실패 : 파일 저장 실패")
    void testHostProductCreate_FileSaveFailed() {
        when(hostProductInfoDto.getCategoryMiddleAltId()).thenReturn(CATEGORY_MIDDLE_ALT_ID);
        when(categoryMiddleRepository.findByAltId(CATEGORY_MIDDLE_ALT_ID))
            .thenReturn(Optional.of(categoryMiddle));
        when(customFileUtil.saveImageFiles(multipartFiles))
            .thenThrow(FileSaveFailedException.class);

        assertThrows(FileSaveFailedException.class, () ->
            hostService
                .hostProductCreate(HOST_ALT_ID.getName(), hostProductInfoDto, multipartFiles)
        );
    }

    @Test
    @DisplayName("호스트 상품 등록 - 실패 : 호스트 찾기 실패")
    void testHostProductCreate_HostNotFound() {
        when(hostProductInfoDto.getCategoryMiddleAltId()).thenReturn(CATEGORY_MIDDLE_ALT_ID);
        when(categoryMiddleRepository.findByAltId(CATEGORY_MIDDLE_ALT_ID))
            .thenReturn(Optional.of(categoryMiddle));
        when(customFileUtil.saveImageFiles(multipartFiles)).thenReturn("imageFileNames");
        when(hostRepository.findByAltId(HOST_ALT_ID.getName()))
            .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () ->
            hostService
                .hostProductCreate(HOST_ALT_ID.getName(), hostProductInfoDto, multipartFiles)
        );
    }
}
