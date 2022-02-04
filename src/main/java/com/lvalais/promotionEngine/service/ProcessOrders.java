package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.Promotion;
import com.lvalais.promotionEngine.domain.SKUOrder;

import java.util.List;

public class ProcessOrders {
    private boolean promosActivated = true;


    public Double processOrder(SKUOrder order, List<Promotion> activePromoList) {
        if (activePromoList==null || activePromoList.isEmpty() ) {
            order.getSkuList().forEach(o -> {
                order.addToOrderTotalWithoutPromo(o.getPrice());
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
