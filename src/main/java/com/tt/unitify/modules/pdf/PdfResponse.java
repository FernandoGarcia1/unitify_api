package com.tt.unitify.modules.pdf;

import lombok.Data;

@Data
public class PdfResponse {
    private byte[] data;
    private String name;
    private double amount;
}
