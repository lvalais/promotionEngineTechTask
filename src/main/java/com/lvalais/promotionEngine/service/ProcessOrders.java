package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.PromotionBase;
import com.lvalais.promotionEngine.domain.SKUBasePrice;
import com.lvalais.promotionEngine.domain.SKUItem;
import com.lvalais.promotionEngine.domain.SKUOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProcessOrders {
    private SKUBasePrice skuBasePrice;

    public ProcessOrders() {
        HashMap<String, Integer> priceMap = new HashMap<>();
        priceMap.put("A", 50);
        priceMap.put("B", 30);
        priceMap.put("C", 20);
        priceMap.put("D", 15);
        skuBasePrice = new SKUBasePrice(priceMap);
    }


    public Double processOrder(SKUOrder order, List<PromotionBase> activePromoList) {
        if (activePromoList != null) {
            //run promotions
            activePromoList.forEach(p -> {
                //check promo is valid, at least one set of this type of promo can be applied
                if (checkPromoValidity(order, p)) {
                    //if more than 1 then we have a multi-item promotion
                    int multiSKUPromoCount = p.getSkuIDInPromoWithCount().size();
                    List<String> skuIDsInThisPromo = new ArrayList<>(p.getSkuIDInPromoWithCount().keySet());
                    if (multiSKUPromoCount == 1) {
                        order.getSkuListWithItemCount().stream().filter(o -> !o.isItemFullyProcessed() && skuIDsInThisPromo.contains(o.getUnit())).forEach(o -> {
                            //first do the promo items
                            order.addToOrderTotalWithPromo((o.getCount() / p.getSkuIDInPromoWithCount().get(o.getUnit())) * p.getAmountAfterDiscount());
                            // do any remaining
                            order.addToOrderTotalWithPromo((o.getCount() % p.getSkuIDInPromoWithCount().get(o.getUnit())) * skuBasePrice.getSkuBasePriceMap().get(o.getUnit()));
                            o.setItemFullyProcessed(true);
                        });
                    } else if (multiSKUPromoCount > 1) {
                        //figure out how many times this promo can run
                        int maxTimesPromoCanRun= Integer.MAX_VALUE;
                        for (Map.Entry<String,Integer> entry : p.getSkuIDInPromoWithCount().entrySet()) {
                            for (SKUItem orderItem : order.getSkuListWithItemCount()) {
                                if (!orderItem.isItemFullyProcessed() && orderItem.getUnit().equals(entry.getKey())){
                                    int count= (orderItem.getCount() / entry.getValue());
                                    if (count<maxTimesPromoCanRun) maxTimesPromoCanRun = count;
                                }
                            }
                       }
                        //first do the promo items
                        order.addToOrderTotalWithPromo((maxTimesPromoCanRun ) * p.getAmountAfterDiscount());
                        for (Map.Entry<String,Integer> entry : p.getSkuIDInPromoWithCount().entrySet()) {
                            for (SKUItem orderItem : order.getSkuListWithItemCount()) {
                                if (!orderItem.isItemFullyProcessed() && orderItem.getUnit().equals(entry.getKey())){
                                   // do any remaining
                                    order.addToOrderTotalWithPromo((orderItem.getCount() -(maxTimesPromoCanRun * entry.getValue())) * skuBasePrice.getSkuBasePriceMap().get(orderItem.getUnit()));
                                    orderItem.setItemFullyProcessed(true);
                                }
                            }
                        }

                    }
                }
            });
            //add rest of items that are not processed under promo
            order.getSkuListWithItemCount().stream().filter(o -> !o.isItemFullyProcessed()).forEach(i -> {
                order.addToOrderTotalWithPromo(skuBasePrice.getSkuBasePriceMap().get(i.getUnit()) * i.getCount());
            });

        } else {
            // no promos
            order.getSkuListWithItemCount().forEach(i -> {
                //keep a sum of price without promo
                order.addToOrderTotalWithoutPromo(skuBasePrice.getSkuBasePriceMap().get(i.getUnit()));
            });
        }

        return activePromoList == null ? order.getOrderTotalWithoutPromo() : order.getOrderTotalWithPromo();
    }


    private boolean checkPromoValidity(SKUOrder order, PromotionBase promo) {
        //get the number of conditions for this promo to be valid
        int requiredCount = promo.getSkuIDInPromoWithCount().size();
        int actualCount = 0;
        for (Map.Entry<String, Integer> entry : promo.getSkuIDInPromoWithCount().entrySet()) {
            for (SKUItem s : order.getSkuListWithItemCount()) {
                if ((s.getUnit().equals(entry.getKey()) && s.getCount() >= entry.getValue() && !s.isItemFullyProcessed())) {
                    actualCount++;
                    if (requiredCount == actualCount) return true;
                }
            }
        }
        return requiredCount == actualCount;
    }
}
