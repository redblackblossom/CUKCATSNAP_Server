package net.catsnap.global.result.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.ResultCode;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ResultCode {
    NOT_FOUND_API(404, "EC000", "존재하지 않는 API 입니다."),
    MISSING_REQUEST_PARAMETER(400, "EC001", "필수 쿼리 파라미터가 누락되었습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
