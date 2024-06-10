package com.refactoringhabit.member.domain.mapper;

import com.refactoringhabit.common.response.MemberInfoDto;
import com.refactoringhabit.member.domain.entity.Member;
import com.refactoringhabit.member.domain.enums.Gender;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T17:11:42+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class MemberEntityMapperImpl implements MemberEntityMapper {

    @Override
    public Member toEntity(MemberJoinRequestDto memberJoinRequestDto, String altId) {
        if ( memberJoinRequestDto == null && altId == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        if ( memberJoinRequestDto != null ) {
            member.password( memberJoinRequestDto.getEncodedPassword() );
            member.email( memberJoinRequestDto.getEmail() );
            member.nickName( memberJoinRequestDto.getNickName() );
            member.phone( memberJoinRequestDto.getPhone() );
            member.birth( memberJoinRequestDto.getBirth() );
            member.profileImage( memberJoinRequestDto.getProfileImage() );
            if ( memberJoinRequestDto.getGender() != null ) {
                member.gender( Enum.valueOf( Gender.class, memberJoinRequestDto.getGender() ) );
            }
        }
        member.altId( generateUuid( altId ) );

        return member.build();
    }

    @Override
    public MemberInfoDto toMemberInfoDto(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberInfoDto.MemberInfoDtoBuilder memberInfoDto = MemberInfoDto.builder();

        if ( member.getType() != null ) {
            memberInfoDto.type( member.getType().name() );
        }
        memberInfoDto.profileImage( member.getProfileImage() );
        memberInfoDto.nickName( member.getNickName() );

        return memberInfoDto.build();
    }
}
