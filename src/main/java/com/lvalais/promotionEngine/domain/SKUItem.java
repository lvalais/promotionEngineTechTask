package com.lvalais.promotionEngine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SKUItem {
    private String unit;
    private int count;
    private boolean itemProcessed = false;
}
