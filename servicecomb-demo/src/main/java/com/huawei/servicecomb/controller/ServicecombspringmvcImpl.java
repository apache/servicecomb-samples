package com.huawei.servicecomb.controller;


import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.apache.servicecomb.provider.rest.common.RestSchema;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.CseSpringDemoCodegen", date = "2019-08-02T08:06:28.094Z")

@RestSchema(schemaId = "servicecombspringmvc")
@RequestMapping(path = "/rest", produces = MediaType.APPLICATION_JSON)
public class ServicecombspringmvcImpl {

    @Autowired
    private ServicecombspringmvcDelegate userServicecombspringmvcDelegate;


    @RequestMapping(value = "/helloworld",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public String helloworld( @RequestParam(value = "name", required = true) String name){

        return userServicecombspringmvcDelegate.helloworld(name);
    }

}
