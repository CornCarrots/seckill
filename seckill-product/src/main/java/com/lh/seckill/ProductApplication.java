package com.lh.seckill;

import brave.sampler.Sampler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableEurekaClient
@EnableDistributedTransaction
@EnableCaching
public class ProductApplication {
    //这个注解为了负载均衡，访问这个服务群，默认轮询访问该服务集群的不同服务
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    public static void main(String[] args) {
        int port = 0;
        int defaultPort = 8011;
        int eurekaServerPort = 8761;
        int txlcnPort = 8070;
        int redisPort = 6379;
        int rabbitMQPort = 5672;
        if (NetUtil.isUsableLocalPort(eurekaServerPort)) {
            System.err.printf("检查到 %d 端口未启用，可能是 Eureka 服务器没有启动，本服务无法使用，故退出 %n", eurekaServerPort);
            System.exit(1);
        }
        if (NetUtil.isUsableLocalPort(txlcnPort)) {
            System.err.printf("检查到 %d 端口未启用，可能是 TX-LCN 没有启动，本服务无法使用，故退出 %n", txlcnPort);
            System.exit(1);
        }
        if (NetUtil.isUsableLocalPort(redisPort)) {
            System.err.printf("检查到 %d 端口未启用，可能是 Redis 没有启动，本服务无法使用，故退出 %n", redisPort);
            System.exit(1);
        }
        if (NetUtil.isUsableLocalPort(rabbitMQPort)) {
            System.err.printf("检查到 %d 端口未启用，可能是 rabbitMQ 服务器没有启动，本服务无法使用，故退出 %n", rabbitMQPort);
            System.exit(1);
        }
        if (null != args && 0 != args.length) {
            for (String arg : args) {
                if (arg.startsWith("port=")) {
                    String strPort = StrUtil.subAfter(arg, "port=", true);
                    if (NumberUtil.isNumber(strPort))
                        port = Convert.toInt(strPort);
                }
            }
        }
        if (0 == port) {
            Future<Integer> future = ThreadUtil.execAsync(() -> {
                int p = 0;
                System.out.printf("请于5秒钟内输入端口号,推荐 %d ,超过5秒将默认使用%d%n", defaultPort, defaultPort);
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String strPort = scanner.nextLine();
                    if (!NumberUtil.isInteger(strPort)) {
                        System.err.println("只能是数字");
                        continue;
                    } else {
                        p = Convert.toInt(strPort);
                        scanner.close();
                        break;
                    }
                }
                return p;
            });
            try {
                port = future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                port = defaultPort;
            }
        }
        if (!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("端口%d被占用了，无法启动%n", port);
            System.exit(1);
        }
        new SpringApplicationBuilder(ProductApplication.class).properties("server.port=" + port).run(args);

    }

}
