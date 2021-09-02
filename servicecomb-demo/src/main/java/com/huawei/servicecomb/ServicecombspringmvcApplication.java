package com.huawei.servicecomb;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class ServicecombspringmvcApplication {
    public static void main(String[] args) {
         SpringApplication.run(ServicecombspringmvcApplication.class,args);
    }
}
