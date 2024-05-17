package com.refactoringhabit.common.exception;

import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    EX001("EX001", "예외 클래스 예시 입니다.",
            CustomException.class, HttpStatus.BAD_REQUEST),
    F001("F001", "파일 저장에 실패했습니다.",
        FileSaveFailedException.class, HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final Class<? extends CustomException> classType;
    private final HttpStatus httpStatus;
    private static final List<ErrorType> errorTypes =
            Arrays.stream(ErrorType.values()).toList();

    public static ErrorType of(Class<? extends CustomException> classType) {
        return errorTypes.stream()
                .filter(it -> it.classType.equals(classType))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
