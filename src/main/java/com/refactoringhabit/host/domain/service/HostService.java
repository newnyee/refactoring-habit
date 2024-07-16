package com.refactoringhabit.host.domain.service;

import static com.refactoringhabit.member.domain.enums.MemberType.HOST;

import com.refactoringhabit.category.domain.entity.CategoryMiddle;
import com.refactoringhabit.category.domain.exception.CategoryNotFoundException;
import com.refactoringhabit.category.domain.repository.CategoryMiddleRepository;
import com.refactoringhabit.common.utils.CustomFileUtil;
import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.host.domain.mapper.HostEntityMapper;
import com.refactoringhabit.host.domain.repository.HostRepository;
import com.refactoringhabit.host.dto.HostInfoResponseDto;
import com.refactoringhabit.host.dto.HostInfoRequestDto;
import com.refactoringhabit.host.dto.HostOptionInfoDto;
import com.refactoringhabit.host.dto.HostProductInfoDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
import com.refactoringhabit.member.domain.repository.MemberRepository;
import com.refactoringhabit.product.domain.mapper.OptionEntityMapper;
import com.refactoringhabit.product.domain.mapper.ProductEntityMapper;
import com.refactoringhabit.product.domain.entity.Product;
import com.refactoringhabit.product.domain.repository.OptionRepository;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.stats.domain.mapper.StatsEntityMapper;
import com.refactoringhabit.stats.domain.repository.HostTotalSalesStatsRepository;
import com.refactoringhabit.stats.domain.repository.ProductTotalSalesStatsRepository;
import java.util.List;
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
    private final CategoryMiddleRepository categoryMiddleRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ProductTotalSalesStatsRepository productTotalSalesStatsRepository;
    private final HostTotalSalesStatsRepository hostTotalSalesStatsRepository;
    private final CustomFileUtil customFileUtil;

    public static final String HOST_ALT_ID = null;
    public static final String PRODUCT_ALT_ID = null;
    public static final String OPTION_ALT_ID = null;
    public static final String STATS_ALT_ID = null;

    @Transactional
    public void hostJoin(String memberAltId, HostInfoRequestDto hostInfoRequestDto,
        MultipartFile multipartFile) {
        Member member = findMember(memberAltId);
        member.setType(HOST);

        HostEntityMapper.INSTANCE.updateHostJoinRequestDtoFromEntity(hostInfoRequestDto, member);
        Host savedHost = hostRepository.save(HostEntityMapper.INSTANCE
            .toEntity(hostInfoRequestDto, HOST_ALT_ID, saveProfileImage(multipartFile), member));

        hostTotalSalesStatsRepository.save(StatsEntityMapper.INSTANCE
            .toHostSalesStatsEntity(savedHost.getId(), STATS_ALT_ID));
    }

    @Transactional(readOnly = true)
    public boolean nickNameCheck(String nickName) {
        return hostRepository.existsByNickName(nickName);
    }

    @Transactional(readOnly = true)
    public HostInfoResponseDto getHostInfo(String memberAltId) {
        return HostEntityMapper.INSTANCE.toHostInfoResponseDto(findMember(memberAltId).getHost());
    }

    @Transactional
    public void hostInfoUpdate(String hostAltId, HostInfoRequestDto hostInfoRequestDto,
        MultipartFile multipartFile) {
        HostEntityMapper.INSTANCE
            .updateEntityFromHostInfoRequestDto(findHost(hostAltId), hostInfoRequestDto,
                saveProfileImage(multipartFile));
    }

    @Transactional
    public void hostProductCreate(String hostAltId, HostProductInfoDto hostProductInfoDto,
        List<MultipartFile> multipartFiles) {
        CategoryMiddle categoryMiddle = categoryMiddleRepository
            .findByAltId(hostProductInfoDto.getCategoryMiddleAltId())
            .orElseThrow(CategoryNotFoundException::new);
        String imageFileNames = customFileUtil.saveImageFiles(multipartFiles);
        Host host = findHost(hostAltId);

        Product savedProduct = productRepository.save(ProductEntityMapper.INSTANCE
            .toEntity(hostProductInfoDto, PRODUCT_ALT_ID, imageFileNames, categoryMiddle, host));

        saveOptions(hostProductInfoDto.getOptionInfoList(), savedProduct);

        productTotalSalesStatsRepository.save(StatsEntityMapper.INSTANCE
            .toProductTotalSalesStatsEntity(host.getId(), savedProduct.getId(),
                categoryMiddle.getId(), STATS_ALT_ID));
    }

    private Member findMember(String memberAltId) {
        return memberRepository.findByAltId(memberAltId).orElseThrow(UserNotFoundException::new);
    }

    private String saveProfileImage(MultipartFile multipartFile) {
        return customFileUtil
            .saveProfileImage(Optional.ofNullable(multipartFile), HOST);
    }

    private Host findHost(String hostAltId) {
        return hostRepository.findByAltId(hostAltId).orElseThrow(UserNotFoundException::new);
    }

    private void saveOptions(List<HostOptionInfoDto> optionInfoDtoList, Product product) {
        optionInfoDtoList.forEach(hostOptionInfoDto ->
            optionRepository.save(
                OptionEntityMapper.INSTANCE
                    .toEntity(hostOptionInfoDto, OPTION_ALT_ID, product))
        );
    }
}
