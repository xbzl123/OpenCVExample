package com.example.opencvexample.uinttest;

import org.junit.Assert;
import org.junit.Test;

public class CaculaterTest {
    Caculater caculater = new Caculater();
    @Test
    public void CacuTest(){
        int result = caculater.add(6, 2);
        Assert.assertEquals(result,3);
    }
}
