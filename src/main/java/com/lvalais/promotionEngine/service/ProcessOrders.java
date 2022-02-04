package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.PromotionBase;
import com.lvalais.promotionEngine.domain.SKUBasePrice;
import com.lvalais.promotionEngine.domain.SKUItem;
import com.lvalais.promotionEngine.domain.SKUOrder;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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
                    int requiredCount=p.getSkuIDInPromoWithCount().size();
                    AtomicInteger actualCount = new AtomicInteger(0);
                    List<Integer> indexListOfPartlyProcessed = new ArrayList<>();
                    order.getSkuListWithItemCount().stream().filter(o-> !o.isItemFullyProcessed()).forEach(o -> {
                        if (p.getSkuIDInPromoWithCount().containsKey(o.getUnit())) {
                            if (requiredCount==actualCount.addAndGet(1)) {
                                //first do the promo items
                                order.addToOrderTotalWithPromo((o.getCount() / p.getSkuIDInPromoWithCount().get(o.getUnit())) * p.getAmountAfterDiscount());
                                // do any remaining
                                order.addToOrderTotalWithPromo((o.getCount() % p.getSkuIDInPromoWithCount().get(o.getUnit())) * skuBasePrice.getSkuBasePriceMap().get(o.getUnit()));
                                o.setItemFullyProcessed(true);
                                // set rest to full
                                if (indexListOfPartlyProcessed.size()==requiredCount-1){
                                    //then this is the last piece of puzzle for this promotion
                                    //go and convert these to fully processed to avoid reprocessing
                                    indexListOfPartlyProcessed.forEach(i->{
                                        order.getSkuListWithItemCount().get(i).setItemFullyProcessed(true);
                                    });
                                }
                            }else{
                                //else do another loop to find the pair? what if there are more required??
                                o.setItemPartiallyProcessed(true);
                                indexListOfPartlyProcessed.add(order.getSkuListWithItemCount().indexOf(o));
                            }
                        }
                    });
                }
            });
            //add rest of items that are not processed under promo
            order.getSkuListWithItemCount().stream().filter(o-> !o.isItemFullyProcessed()).forEach(i -> {
                order.addToOrderTotalWithPromo(skuBasePrice.getSkuBasePriceMap().get(i.getUnit())*i.getCount());
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
                if ((s.getUnit().equals(entry.getKey()) && s.getCount() >= entry.getValue() && !s.isItemFullyProcessed())) {
                    actualCount++;
                    if (requiredCount==actualCount) return true;
                }
            }
        }
        return requiredCount == actualCount;
    }
}
