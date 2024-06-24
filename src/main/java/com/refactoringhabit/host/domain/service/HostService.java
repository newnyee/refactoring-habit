package com.refactoringhabit.host.domain.service;

import static com.refactoringhabit.host.domain.mapper.HostEntityMapper.INSTANCE;
import static com.refactoringhabit.member.domain.enums.MemberType.HOST;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.host.domain.repository.HostRepository;
import com.refactoringhabit.host.dto.HostJoinRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class HostService {

    private final MemberRepository memberRepository;
    private final HostRepository hostRepository;
    private final CustomFileUtil customFileUtil;

    public static final String HOST_ALT_ID = null;

    @Transactional
    public void hostJoin(String memberAltId, HostJoinRequestDto hostJoinRequestDto,
        MultipartFile multipartFile) {

        Member member = memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);
        member.setType(HOST);
        INSTANCE.updateHostJoinRequestDtoFromEntity(hostJoinRequestDto, member);

        try {
            String getFileName = customFileUtil.saveProfileImage(Optional.ofNullable(multipartFile), HOST);
            hostRepository.save(
                INSTANCE.toEntity(hostJoinRequestDto, HOST_ALT_ID, getFileName, member));
        } catch (IOException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileSaveFailedException();
        }
    }

    @Transactional(readOnly = true)
    public boolean nickNameCheck(String nickName) {
        return hostRepository.existsByNickName(nickName);
    }
}
