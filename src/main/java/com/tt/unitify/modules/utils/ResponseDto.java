package com.tt.unitify.modules.utils;

import lombok.Data;

@Data
public class ResponseDto {
    String message;

    public ResponseDto(String message) {
        this.message = message;
    }
}
