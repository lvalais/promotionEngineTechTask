package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.PromotionBase;
import com.lvalais.promotionEngine.domain.SKUBasePrice;
import com.lvalais.promotionEngine.domain.SKUItem;
import com.lvalais.promotionEngine.domain.SKUOrder;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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



        if (activePromoList!=null) {
            //run promotions
            activePromoList.forEach(p -> {
                //check promo is valid
                if (checkPromoValidity(order, p)) {
                    order.getSkuListWithItemCount().forEach(o -> {
                        if (p.getSkuIDInPromoWithCount().containsKey(o.getUnit())) {
                            //first do the promo items 5/3 =2
                            order.addToOrderTotalWithPromo((o.getCount() / p.getSkuIDInPromoWithCount().get(o.getUnit())) * p.getAmountAfterDiscount());
                            // do remaining
                            order.addToOrderTotalWithPromo((o.getCount() % p.getSkuIDInPromoWithCount().get(o.getUnit())) * skuBasePrice.getSkuBasePriceMap().get(o.getUnit()));
                            o.setItemProcessed(true);
                        }
                    });
                }

            });
            order.getSkuListWithItemCount().stream().filter(o-> !o.isItemProcessed()).forEach(i -> {
                //keep a sum of price without promo
                order.addToOrderTotalWithPromo(skuBasePrice.getSkuBasePriceMap().get(i.getUnit()));
            });

        }else{
            // no promos
            order.getSkuListWithItemCount().forEach(i -> {
                //keep a sum of price without promo
                order.addToOrderTotalWithoutPromo(skuBasePrice.getSkuBasePriceMap().get(i.getUnit()));
            });
        }

        return activePromoList==null? order.getOrderTotalWithoutPromo() : order.getOrderTotalWithPromo();
    }


    private boolean checkPromoValidity(SKUOrder order, PromotionBase promo){
        //get the number of conditions for this promo to be valid
        int requiredCount= promo.getSkuIDInPromoWithCount().size();
        int actualCount = 0;
        for (Map.Entry<String, Integer> entry: promo.getSkuIDInPromoWithCount().entrySet()) {
            for (SKUItem s : order.getSkuListWithItemCount()) {
                if ((s.getUnit().equals(entry.getKey()) && s.getCount() >= entry.getValue())) {
                    actualCount++;
                    if (requiredCount==actualCount) return true;
                }
            }
        }
        return requiredCount == actualCount;
    }
}
