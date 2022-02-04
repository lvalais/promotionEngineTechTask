package com.lvalais.promotionEngine.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Promotion {
    private int promotionID;
    private List<SKU> skuInPromo;


}
