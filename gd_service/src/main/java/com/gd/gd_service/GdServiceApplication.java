package com.gd.gd_service;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Log4j2
@EnableCaching //启动缓存注解
@EntityScan(value={"com.gd.base.entity,com.gd.base.entity_second"})
@ComponentScan(value = {"com.gd.**"},basePackages = {"com.gd.**"})
//@EnableJpaRepositories(basePackages = {"com.gd.base.jpa","com.gd.base.jpa_second"})//Srping JPA的代码配置，用于取代xml形式的配置文件,配置多数据源需要取消
@SpringBootApplication
//Spring Boot会自动根据你jar包的依赖来自动配置项目，不使用自动添加的jar
//@EnableAutoConfiguration(exclude = {JndiConnectionFactoryAutoConfiguration.class, DataSourceAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@EnableTransactionManagement //开启事务注解功能
public class GdServiceApplication {

    public static void main(String[] args) {
        //SpringApplication.run(GdServiceApplication.class, args);
        ConfigurableApplicationContext application = SpringApplication.run(GdServiceApplication.class, args);
        Environment env = application.getEnvironment();
        //各个地址打印
        logApplicationStartup(env);
    }
    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath.isEmpty()) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "swagger: \t{}://{}:{}{}/doc.html\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }


}
