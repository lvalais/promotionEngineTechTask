package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.PromotionBase;
import com.lvalais.promotionEngine.domain.SKUBasePrice;
import com.lvalais.promotionEngine.domain.SKUOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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
        List<String> skuList = new ArrayList<>();
        skuList.add("A");
        skuList.add("B");
        skuList.add("C");
        skuOrder.setSkuList(skuList);
        assertEquals(100,processOrders.processOrder(skuOrder, null));
    }

    @Test
    public void processOrderTestScenarioB(){
        List<String> skuList = new ArrayList<>();
        skuList.add("A");
        skuList.add("A");
        skuList.add("A");
        skuList.add("A");
        skuList.add("A");

        skuList.add("B");
        skuList.add("B");
        skuList.add("B");
        skuList.add("B");
        skuList.add("B");

        skuList.add("C");
//        skuList.add(new SKU("D",15));
        skuOrder.setSkuList(skuList);
        assertEquals(370,processOrders.processOrder(skuOrder, null));
    }

    @Test
    public void processOrderTestScenarioC(){
        List<String> skuList = new ArrayList<>();
        skuList.add("A");
        skuList.add("A");
        skuList.add("A");

        skuList.add("B");
        skuList.add("B");
        skuList.add("B");
        skuList.add("B");
        skuList.add("B");

        skuList.add("C");
        skuList.add("D");
        skuOrder.setSkuList(skuList);
        assertEquals(280,processOrders.processOrder(skuOrder, null));
    }


}