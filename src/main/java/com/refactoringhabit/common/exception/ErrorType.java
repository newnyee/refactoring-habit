package com.refactoringhabit.common.exception;

import com.refactoringhabit.auth.domain.exception.AuthTokenExpiredException;
import com.refactoringhabit.auth.domain.exception.EmailingException;
import com.refactoringhabit.auth.domain.exception.InvalidTokenException;
import com.refactoringhabit.auth.domain.exception.NullTokenException;
import com.refactoringhabit.auth.domain.exception.PasswordNotMatchException;
import com.refactoringhabit.host.domain.exception.NotFoundBankException;
import com.refactoringhabit.member.domain.exception.FileSaveFailedException;
import com.refactoringhabit.member.domain.exception.NotFoundEmailException;
import com.refactoringhabit.auth.domain.exception.NotHostException;
import com.refactoringhabit.member.domain.exception.UserNotFoundException;
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
        FileSaveFailedException.class, HttpStatus.INTERNAL_SERVER_ERROR),

    U001("U001", "이메일을 찾을 수 없습니다.",
        NotFoundEmailException.class, HttpStatus.NOT_FOUND),
    U002("U002", "회원 정보를 찾을 수 없습니다.",
        UserNotFoundException.class, HttpStatus.NOT_FOUND),

    A001("A001", "이메일 발송에 실패하였습니다.",
        EmailingException.class, HttpStatus.INTERNAL_SERVER_ERROR),
    A002("T002", "토큰을 입력해주세요.",
        NullTokenException.class, HttpStatus.BAD_REQUEST),
    A003("T003", "유효하지 않은 토큰입니다.",
        InvalidTokenException.class, HttpStatus.UNAUTHORIZED),
    A004("T004", "만료된 토큰입니다.",
        AuthTokenExpiredException.class, HttpStatus.UNAUTHORIZED),
    A005("A005", "패스워드가 일치하지 않습니다.",
        PasswordNotMatchException.class, HttpStatus.UNAUTHORIZED),
    A006("A006", "호스트가 아닙니다.",
        NotHostException.class, HttpStatus.FORBIDDEN),

    H001("H001", "은행을 찾을 수 없습니다.",
        NotFoundBankException.class, HttpStatus.NOT_FOUND);

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
