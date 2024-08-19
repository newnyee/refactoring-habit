package com.refactoringhabit.product.domain.repository;

import com.refactoringhabit.product.dto.OptionDetailDto;
import java.util.List;

public interface OptionRepositoryCustom {
    List<OptionDetailDto> getOptionDetailDtos(String productAltId);
}
