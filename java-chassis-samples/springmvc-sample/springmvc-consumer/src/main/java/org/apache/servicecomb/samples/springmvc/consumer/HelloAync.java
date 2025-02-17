package org.apache.servicecomb.samples.springmvc.consumer;

import org.apache.servicecomb.samples.common.schema.models.Person;

import java.util.concurrent.CompletableFuture;


public interface HelloAync {
    CompletableFuture<String> sayHi(String name);

    CompletableFuture<String> sayHello(Person person);
}
