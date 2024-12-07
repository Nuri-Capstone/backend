package com.nuri.nuribackend.dto.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message; // 오류 메시지
    private Object data;    // 추가 데이터 (필요하지 않을 경우 null 가능)

    public static ErrorResponse of(String message, Object data) {
        return new ErrorResponse(message, data);
    }
}
