package com.refactoringhabit.product.domain.mapper;

import com.refactoringhabit.host.dto.HostOptionInfoDto;
import com.refactoringhabit.product.domain.entity.Option;
import com.refactoringhabit.product.domain.entity.Product;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T17:08:58+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class OptionEntityMapperImpl implements OptionEntityMapper {

    @Override
    public Option toEntity(HostOptionInfoDto dto, String optionAltId, Product product) {
        if ( dto == null && optionAltId == null && product == null ) {
            return null;
        }

        Option.OptionBuilder option = Option.builder();

        if ( dto != null ) {
            option.name( dto.getName() );
            option.quantity( dto.getQuantity() );
            option.price( dto.getPrice() );
        }
        option.altId( generateUuid( optionAltId ) );
        option.product( product );

        return option.build();
    }
}
