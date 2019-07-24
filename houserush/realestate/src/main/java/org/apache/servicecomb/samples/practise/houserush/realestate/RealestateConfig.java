
package org.apache.servicecomb.samples.practise.houserush.realestate;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.apache.servicecomb.samples.practise.houserush")
public class RealestateConfig {
}
