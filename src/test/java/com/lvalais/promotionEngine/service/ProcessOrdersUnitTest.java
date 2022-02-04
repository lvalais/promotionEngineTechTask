package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.PromotionBase;
import com.lvalais.promotionEngine.domain.SKUItem;
import com.lvalais.promotionEngine.domain.SKUOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProcessOrdersUnitTest {
    private ProcessOrders processOrders = new ProcessOrders();
    private SKUOrder skuOrder;
    private PromotionBase p1;
    private PromotionBase p2;
    private PromotionBase p3;
    private PromotionBase p4;

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
        //create promotion D
        HashMap<String, Integer> promoHashMapD = new HashMap<>();
        promoHashMapD.put("A",1);
        promoHashMapD.put("C",1);
        promoHashMapD.put("D",1);
        p4 = new PromotionBase(4,promoHashMapD,18);
    }

    @Test
    public void processOrderTestScenarioTestNullPromoList(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",1, false,false));
        skuList.add(new SKUItem("B",1,false,false));
        skuList.add(new SKUItem("C",1,false,false));
        skuOrder.setSkuListWithItemCount(skuList);
        assertEquals(100,processOrders.processOrder(skuOrder, null));
    }

    @Test
    public void processOrderTestScenarioSKUItemWithZeroCount(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",5, false,false));
        skuList.add(new SKUItem("B",5,false,false));
        skuList.add(new SKUItem("C",1,false,false));
        skuList.add(new SKUItem("D",0,false,false));
        skuOrder.setSkuListWithItemCount(skuList);
        assertEquals(370,processOrders.processOrder(skuOrder, List.of(p1,p2,p3)));
    }

    @Test
    public void processOrderTestScenarioOneCLeftOutOfPromo(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",3, false,false));
        skuList.add(new SKUItem("B",5,false,false));
        skuList.add(new SKUItem("C",5,false,false));
        skuList.add(new SKUItem("D",4,false,false));
        skuOrder.setSkuListWithItemCount(skuList);
        assertEquals(390,processOrders.processOrder(skuOrder, List.of(p1,p2,p3)));
    }

    @Test
    public void processOrderTestScenarioCaseDWillNotBeValid(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",5, false,false));
        skuList.add(new SKUItem("B",5,false,false));
        skuList.add(new SKUItem("C",6,false,false));
        skuList.add(new SKUItem("D",4,false,false));
        skuOrder.setSkuListWithItemCount(skuList);
        assertEquals(510,processOrders.processOrder(skuOrder, List.of(p1,p2,p3,p4)));
    }

    @Test
    public void checkPromoValidityTest(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",5, false,false));
        skuList.add(new SKUItem("B",5,false,false));
        skuList.add(new SKUItem("C",6,false,false));
        skuList.add(new SKUItem("D",4,false,false));
        skuOrder.setSkuListWithItemCount(skuList);
        assertTrue(processOrders.checkPromoValidity(skuOrder, p4));
    }
    @Test
    public void checkPromoValidityTestFail(){
        List<SKUItem> skuList = new ArrayList<>();
        skuList.add(new SKUItem("A",5, false,false));
        skuList.add(new SKUItem("B",5,false,false));
        skuList.add(new SKUItem("C",6,false,false));
        skuOrder.setSkuListWithItemCount(skuList);
        assertFalse(processOrders.checkPromoValidity(skuOrder, p4));
    }


}