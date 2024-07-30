package com.xiyue.admin;

import cn.dev33.satoken.SaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class XiyueAdminApplication {

	public static void main(String[] args) {
		SpringApplication app=new SpringApplication(XiyueAdminApplication.class);
		ConfigurableApplicationContext application=app.run(args);
		Environment env = application.getEnvironment();
		try {
			log.info("\n" + String.format("""
					----------------------------------------------------------
					     Application '%s' is running! Access URLs:
					     Local:     http://localhost:%s
					     External: http://%s:%s
					     Doc: http://%s:%s/doc.html
					----------------------------------------------------------
			        """,
					env.getProperty("spring.application.name"),
					env.getProperty("server.port"),
					InetAddress.getLocalHost().getHostAddress(),
					env.getProperty("server.port"),
					InetAddress.getLocalHost().getHostAddress(),
					env.getProperty("server.port")));
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}

		log.info("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
	}
}
