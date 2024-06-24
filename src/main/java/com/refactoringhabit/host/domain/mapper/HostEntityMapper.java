package com.refactoringhabit.host.domain.mapper;

import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.host.dto.HostJoinRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HostEntityMapper {

    HostEntityMapper INSTANCE = Mappers.getMapper(HostEntityMapper.class);

    @Mapping(target = "altId", source = "altId", qualifiedByName = "generateUuid")
    @Mapping(target = "nickName", source = "dto.nickName")
    @Mapping(target = "phone", source = "dto.phone")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "profileImage", source = "profileImage")
    Host toEntity(
        HostJoinRequestDto dto, String altId, String profileImage, Member member);

    @Mapping(target = "nickName", expression = "java((dto.getNickName() == null || dto.getNickName().equals(\"\")) ? member.getNickName() : dto.getNickName())")
    @Mapping(target = "phone", expression = "java((dto.getPhone() == null || dto.getPhone().equals(\"\")) ? member.getPhone() : dto.getPhone())")
    @Mapping(target = "email", expression = "java((dto.getEmail() == null || dto.getEmail().equals(\"\")) ? member.getEmail() : dto.getEmail())")
    void updateHostJoinRequestDtoFromEntity(
        @MappingTarget HostJoinRequestDto dto, Member member);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
