package com.refactoringhabit.member.domain.mapper;

import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberEntityMapper {

    MemberEntityMapper INSTANCE = Mappers.getMapper(MemberEntityMapper.class);

    @Mapping(source = "mid", target = "mid", qualifiedByName = "generateUuid")
    Member toEntity(MemberJoinRequestDto memberJoinRequestDto, String mid);

    @Named("generateUuid")
    default String generateUuid(String value) {
        return (value == null) ? UUID.randomUUID().toString() : value;
    }
}
