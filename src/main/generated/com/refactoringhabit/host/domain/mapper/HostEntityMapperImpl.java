package com.refactoringhabit.host.domain.mapper;

import com.refactoringhabit.host.domain.entity.Host;
import com.refactoringhabit.host.dto.HostInfoResponseDto;
import com.refactoringhabit.host.dto.HostJoinRequestDto;
import com.refactoringhabit.member.domain.entity.Member;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T18:54:33+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class HostEntityMapperImpl implements HostEntityMapper {

    @Override
    public Host toEntity(HostJoinRequestDto dto, String altId, String profileImage, Member member) {
        if ( dto == null && altId == null && profileImage == null && member == null ) {
            return null;
        }

        Host.HostBuilder host = Host.builder();

        if ( dto != null ) {
            host.nickName( dto.getNickName() );
            host.phone( dto.getPhone() );
            host.email( dto.getEmail() );
            host.introduction( dto.getIntroduction() );
            host.accountNumber( dto.getAccountNumber() );
            host.bank( dto.getBank() );
            host.accountHolder( dto.getAccountHolder() );
        }
        host.altId( generateUuid( altId ) );
        host.profileImage( profileImage );
        host.member( member );

        return host.build();
    }

    @Override
    public void updateHostJoinRequestDtoFromEntity(HostJoinRequestDto dto, Member member) {
        if ( member == null ) {
            return;
        }

        dto.setNickName( (dto.getNickName() == null || dto.getNickName().equals("")) ? member.getNickName() : dto.getNickName() );
        dto.setPhone( (dto.getPhone() == null || dto.getPhone().equals("")) ? member.getPhone() : dto.getPhone() );
        dto.setEmail( (dto.getEmail() == null || dto.getEmail().equals("")) ? member.getEmail() : dto.getEmail() );
    }

    @Override
    public HostInfoResponseDto toHostInfoResponseDto(Host host) {
        if ( host == null ) {
            return null;
        }

        HostInfoResponseDto.HostInfoResponseDtoBuilder hostInfoResponseDto = HostInfoResponseDto.builder();

        hostInfoResponseDto.altId( host.getAltId() );
        hostInfoResponseDto.nickName( host.getNickName() );
        hostInfoResponseDto.phone( host.getPhone() );
        hostInfoResponseDto.profileImage( host.getProfileImage() );
        hostInfoResponseDto.email( host.getEmail() );
        hostInfoResponseDto.introduction( host.getIntroduction() );
        hostInfoResponseDto.accountNumber( host.getAccountNumber() );
        hostInfoResponseDto.bank( host.getBank() );
        hostInfoResponseDto.accountHolder( host.getAccountHolder() );

        return hostInfoResponseDto.build();
    }
}
