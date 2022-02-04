package com.lvalais.promotionEngine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PromotionBase {
    private int promotionID;
    private Map<String,Integer> skuIDInPromoWithCount;
    private Integer amountAfterDiscount;
}
