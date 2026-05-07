package ewm.feature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "ewm")
@EntityScan(basePackages = {"ewm.category", "ewm.comment", "ewm.compilation"})
@EnableJpaRepositories(basePackages = {"ewm.category", "ewm.comment", "ewm.compilation"})
@SpringBootApplication(scanBasePackages = "ewm")
public class FeatureServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeatureServiceApplication.class, args);
    }
}
