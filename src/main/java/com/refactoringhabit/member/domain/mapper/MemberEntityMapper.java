package com.refactoringhabit.member.domain.mapper;

import com.refactoringhabit.common.response.MemberInfoDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.dto.MemberInfoResponseDto;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import com.refactoringhabit.member.dto.MemberUpdateInfoRequestDto;
import java.util.UUID;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberEntityMapper {

    MemberEntityMapper INSTANCE = Mappers.getMapper(MemberEntityMapper.class);

    @Mapping(target = "altId", qualifiedByName = "generateUuid")
    @Mapping(source = "memberJoinRequestDto.encodedPassword", target = "password")
    Member toEntity(MemberJoinRequestDto memberJoinRequestDto, String altId);

    MemberInfoDto toMemberInfoDto(Member member);

    MemberInfoResponseDto toMemberInfoResponseDto(Member member);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MemberUpdateInfoRequestDto memberUpdateInfoRequestDto,
        String encodedPassword, String profileImage, @MappingTarget Member member);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
