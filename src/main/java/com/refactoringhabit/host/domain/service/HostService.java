package com.refactoringhabit.host.domain.service;

import static com.refactoringhabit.member.domain.enums.MemberType.HOST;

import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.host.domain.mapper.HostEntityMapper;
import com.refactoringhabit.host.domain.repository.HostRepository;
import com.refactoringhabit.host.dto.HostInfoResponseDto;
import com.refactoringhabit.host.dto.HostInfoRequestDto;
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
    public void hostJoin(String memberAltId, HostInfoRequestDto hostInfoRequestDto,
        MultipartFile multipartFile) {

        Member member = memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);
        member.setType(HOST);
        HostEntityMapper.INSTANCE.updateHostJoinRequestDtoFromEntity(hostInfoRequestDto, member);

        try {
            String getFileName = saveProfileImage(multipartFile);
            hostRepository.save(HostEntityMapper.INSTANCE
                .toEntity(hostInfoRequestDto, HOST_ALT_ID, getFileName, member));
        } catch (IOException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileSaveFailedException();
        }
    }

    @Transactional(readOnly = true)
    public boolean nickNameCheck(String nickName) {
        return hostRepository.existsByNickName(nickName);
    }

    @Transactional(readOnly = true)
    public HostInfoResponseDto getHostInfo(String memberAltId) {
        Member member = memberRepository.findByAltId(memberAltId)
            .orElseThrow(UserNotFoundException::new);
        return HostEntityMapper.INSTANCE.toHostInfoResponseDto(member.getHost());
    }

    @Transactional
    public void hostInfoUpdate(String hostAltId, HostInfoRequestDto hostInfoRequestDto,
        MultipartFile multipartFile) {

        Host host = hostRepository.findByAltId(hostAltId).orElseThrow(UserNotFoundException::new);

        try {
            String getFileName = saveProfileImage(multipartFile);
            HostEntityMapper.INSTANCE
                .updateEntityFromHostInfoRequestDto(host, hostInfoRequestDto, getFileName);
        } catch (IOException e) {
            log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileSaveFailedException();
        }
    }

    private String saveProfileImage(MultipartFile multipartFile) throws IOException {
        return customFileUtil
            .saveProfileImage(Optional.ofNullable(multipartFile), HOST);
    }
}
