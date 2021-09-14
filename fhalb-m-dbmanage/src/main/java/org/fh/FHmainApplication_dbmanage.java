package org.fh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 说明：启动类 
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@SpringBootApplication
@MapperScan("org.fh.mapper")
@EnableCaching
@EnableDiscoveryClient
@EnableRedisHttpSession
public class FHmainApplication_dbmanage {

	public static void main(String[] args) {
		SpringApplication.run(FHmainApplication_dbmanage.class, args);
	}
	
}