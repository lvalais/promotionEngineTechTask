package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.PromotionBase;
import com.lvalais.promotionEngine.domain.SKUBasePrice;
import com.lvalais.promotionEngine.domain.SKUOrder;

import java.util.HashMap;
import java.util.List;

public class ProcessOrders {
    private boolean promosActivated = true;
    private SKUBasePrice skuBasePrice;

    public ProcessOrders(){
        HashMap<String, Integer> priceMap = new HashMap<>();
        priceMap.put("A",50);
        priceMap.put("B",30);
        priceMap.put("C",20);
        priceMap.put("D",15);
        skuBasePrice = new SKUBasePrice(priceMap);
    }


    public Double processOrder(SKUOrder order, List<PromotionBase> activePromoList) {
        if (activePromoList==null || activePromoList.isEmpty() ) {
            order.getSkuList().forEach(o -> {
                order.addToOrderTotalWithoutPromo(skuBasePrice.getSkuBasePriceMap().get(o));
            });
        }else {
//            order.getSkuList().forEach(o -> {
//
//
//            });
        }

        return order.getOrderTotalWithoutPromo();
    }
}
