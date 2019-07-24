package org.apache.servicecomb.samples.practise.houserush.realestate;

import org.apache.servicecomb.common.rest.codec.RestObjectMapperFactory;
import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@SpringBootApplication
@EnableServiceComb
public class RealestateApplication {

    public static void main(String[] args) {
        configBeforeBoot();
        SpringApplication.run(RealestateApplication.class, args);
    }

    private static void configBeforeBoot() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        RestObjectMapperFactory.getRestObjectMapper().setDateFormat(simpleDateFormat);
    }
}
