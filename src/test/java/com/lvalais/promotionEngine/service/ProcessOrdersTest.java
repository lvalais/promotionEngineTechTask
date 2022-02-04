package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.PromotionBase;
import com.lvalais.promotionEngine.domain.SKUBasePrice;
import com.lvalais.promotionEngine.domain.SKUItem;
import com.lvalais.promotionEngine.domain.SKUOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProcessOrdersTest {
    private ProcessOrders processOrders = new ProcessOrders();
    private SKUOrder skuOrder;
    private List<PromotionBase> activePromotions;
    private PromotionBase p1;
    private PromotionBase p2;
    private PromotionBase p3;


    @BeforeEach
    void setUp() {
        skuOrder = new SKUOrder();
        //create promotion A
        HashMap<String, Integer> promoHashMapA = new HashMap<>();
        promoHashMapA.put("A",3);
        p1 = new PromotionBase(1,promoHashMapA,130);

        //create promotion B
        HashMap<String, Integer> promoHashMapB = new HashMap<>();
        promoHashMapB.put("B",2);
        p2 = new PromotionBase(2,promoHashMapB,45);

        //create promotion C
        HashMap<String, Integer> promoHashMapC = new HashMap<>();
        promoHashMapC.put("C",1);
        promoHashMapC.put("D",1);
        p3 = new PromotionBase(3,promoHashMapC,30);

    }

    @Test
    public void processOrderTestScenarioA(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",1, false));
        skuList.add(new SKUItem("B",1,false));
        skuList.add(new SKUItem("C",1,false));
        skuOrder.setSkuListWithItemCount(skuList);
        assertEquals(100,processOrders.processOrder(skuOrder, List.of(p1,p2,p3)));
    }

    @Test
    public void processOrderTestScenarioB(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",5, false));
        skuList.add(new SKUItem("B",5,false));
        skuList.add(new SKUItem("C",1,false));
        skuOrder.setSkuListWithItemCount(skuList);


        assertEquals(370,processOrders.processOrder(skuOrder, List.of(p1,p2,p3)));
    }

    @Test
    public void processOrderTestScenarioC(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",3, false));
        skuList.add(new SKUItem("B",5,false));
        skuList.add(new SKUItem("C",1,false));
        skuList.add(new SKUItem("D",1,false));
        skuOrder.setSkuListWithItemCount(skuList);
        assertEquals(280,processOrders.processOrder(skuOrder, List.of(p1,p2,p3)));
    }


}