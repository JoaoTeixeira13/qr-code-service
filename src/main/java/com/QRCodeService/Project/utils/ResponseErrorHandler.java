package com.QRCodeService.Project.utils;

import org.springframework.http.ResponseEntity;

public class ResponseErrorHandler {

    private final ResponseEntity<String> responseEntity;

    public ResponseErrorHandler(String errorMessage) {
        responseEntity = ResponseEntity.badRequest()
                .body("{\"error\": \"%s\"}".formatted(errorMessage));
    }

    public ResponseEntity<String> getResponseEntity() {
        return this.responseEntity;
    }
}
