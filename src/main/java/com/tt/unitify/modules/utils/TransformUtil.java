package com.tt.unitify.modules.utils;

import com.google.firebase.database.utilities.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class TransformUtil {
    public static final String UTILITY_CLASS = "Utility class";

    private TransformUtil() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static ResponseEntity file(Optional<Pair<String, byte[]>> body) {
        return body.map(TransformUtil::okFile).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
    public static ResponseEntity okFile(Pair<String, byte[]> body) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + body.getFirst())
                .body(body.getSecond());
    }
}
