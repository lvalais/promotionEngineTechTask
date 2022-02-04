package com.lvalais.promotionEngine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SKUBasePrice {
    private Map<String, Integer> skuBasePriceMap = new HashMap<>();
}
