package com.huawei.servicecomb.controller;



import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestServicecombspringmvc {

        ServicecombspringmvcDelegate servicecombspringmvcDelegate = new ServicecombspringmvcDelegate();


    @Test
    public void testhelloworld(){

        String expactReturnValue = "hello"; // You should put the expect String type value here.

        String returnValue = servicecombspringmvcDelegate.helloworld("hello");

        assertEquals(expactReturnValue, returnValue);
    }

}