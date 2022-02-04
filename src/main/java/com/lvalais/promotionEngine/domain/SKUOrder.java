package com.lvalais.promotionEngine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor()
@NoArgsConstructor
@ToString
public class SKUOrder {
    private String orderUUID = UUID.randomUUID().toString();
    private Map<String,Integer> skuListWithItemCount;
    private Double orderTotalWithoutPromo = 0.0;
    private Double orderTotalWithPromo = 0.0;

    public void addToOrderTotalWithoutPromo(Integer price){
        orderTotalWithoutPromo += price;
    }
    public void addToOrderTotalWithPromo(Integer price){
        orderTotalWithPromo += price;
    }
}
