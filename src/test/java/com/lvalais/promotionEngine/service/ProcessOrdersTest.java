package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.PromotionBase;
import com.lvalais.promotionEngine.domain.SKUBasePrice;
import com.lvalais.promotionEngine.domain.SKUOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProcessOrdersTest {
    private ProcessOrders processOrders = new ProcessOrders();
    private SKUOrder skuOrder;
    private List<PromotionBase> activePromotions;

    @BeforeEach
    void setUp() {
        skuOrder = new SKUOrder();

    }

    @Test
    public void processOrderTestScenarioA(){
        Map<String,Integer> skuMap = new HashMap();
        skuMap.put("A",1);
        skuMap.put("B",1);
        skuMap.put("C",1);
        skuOrder.setSkuListWithItemCount(skuMap);
        assertEquals(100,processOrders.processOrder(skuOrder, null));
    }

    @Test
    public void processOrderTestScenarioB(){
        Map<String,Integer> skuMap = new HashMap();
        skuMap.put("A",5);
        skuMap.put("B",5);
        skuMap.put("C",1);
        skuOrder.setSkuListWithItemCount(skuMap);

        //create promotion A
        HashMap<String, Integer> promoHashMap = new HashMap<>();
        promoHashMap.put("A",3);
        PromotionBase p1 = new PromotionBase();


        assertEquals(370,processOrders.processOrder(skuOrder, null));
    }

    @Test
    public void processOrderTestScenarioC(){
        Map<String,Integer> skuMap = new HashMap();
        skuMap.put("A",3);
        skuMap.put("B",5);
        skuMap.put("C",1);
        skuMap.put("D",1);
        skuOrder.setSkuListWithItemCount(skuMap);
        assertEquals(280,processOrders.processOrder(skuOrder, null));
    }


}