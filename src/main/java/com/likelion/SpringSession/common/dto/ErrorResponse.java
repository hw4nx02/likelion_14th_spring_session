package com.likelion.SpringSession.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 응답 모양
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;
}
