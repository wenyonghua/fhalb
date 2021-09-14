package org.fh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

/**
 * 说明：启动类 
 * 作者：FH Admin Q313596790 
 * 官网：www.fhadmin.org
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class FHmainApplication_bootAdmin {

	public static void main(String[] args) {
		SpringApplication.run(FHmainApplication_bootAdmin.class, args);
	}
	
}