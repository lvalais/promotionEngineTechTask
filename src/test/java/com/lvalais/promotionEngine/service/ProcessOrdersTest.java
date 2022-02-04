package com.lvalais.promotionEngine.service;

import com.lvalais.promotionEngine.domain.Promotion;
import com.lvalais.promotionEngine.domain.SKU;
import com.lvalais.promotionEngine.domain.SKUOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProcessOrdersTest {
    private ProcessOrders processOrders = new ProcessOrders();
    private SKUOrder skuOrder;
    private List<Promotion> activePromotions;

    @BeforeEach
    void setUp() {
        skuOrder = new SKUOrder();
        List<SKU> skuList = new ArrayList<>();
        skuList.add(new SKU("A",50));
        skuList.add(new SKU("B",30));
        skuList.add(new SKU("C",20));
//        skuList.add(new SKU("D",15));
        skuOrder.setSkuList(skuList);
    }

    @Test
    public void processOrderTestScenarioA(){
        assertEquals(100,processOrders.processOrder(skuOrder, null));
    }


}