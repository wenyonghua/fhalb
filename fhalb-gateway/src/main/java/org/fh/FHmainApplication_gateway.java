package org.fh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 说明：启动类 
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FHmainApplication_gateway {

	public static void main(String[] args) {
		SpringApplication.run(FHmainApplication_gateway.class, args);
	}
	
}