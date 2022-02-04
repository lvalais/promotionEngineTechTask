package com.lvalais.promotionEngine.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SKUOrder {
    private String orderUUID = UUID.randomUUID().toString();
    private List<SKU> skuList;
    private Double orderTotalWithoutPromo;
    private Double orderTotalWithPromo;
}
