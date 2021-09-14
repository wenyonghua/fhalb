package org.fh.util;

import java.util.Map;

import org.fh.entity.PageData;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 说明：请求微服务
 * 作者：FH Admin Q 313-596-790
 * 官网：www.fhadmin.org
 */
public class LoadBalancerUtil {
	
	/**POST请求
	 * @param loadBalancerClient
	 * @param providerName			//服务名称
	 * @param requestMappingPath	//请求地址
	 * @param pd					//携带的参数
	 * @return
	 * @throws Exception
	 */
	public static Object responseByPost(LoadBalancerClient loadBalancerClient, String providerName, String requestMappingPath, PageData pd)throws Exception{
		ServiceInstance si = loadBalancerClient.choose(providerName); //ServiceInstance 封装了服务的基本信息，如 IP，端口
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(si.getHost()).append(":").append(si.getPort()).append("/").append(requestMappingPath);
		RestTemplate rt = new RestTemplate();		//springMVC RestTemplate
		ParameterizedTypeReference<Object> type = new ParameterizedTypeReference<Object>(){};
        HttpHeaders headers = new HttpHeaders();	//请求头
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
	    HttpEntity<PageData> httpEntity = new HttpEntity<PageData>(pd, headers);
		ResponseEntity<Object> response = rt.exchange(sb.toString(),HttpMethod.POST, httpEntity, type);
		return response.getBody();
	}
	
	/**POST请求
	 * @param loadBalancerClient
	 * @param providerName			//服务名称
	 * @param requestMappingPath	//请求地址
	 * @param pd					//携带的参数
	 * @return
	 * @throws Exception
	 */
	public static Object responseByPost(LoadBalancerClient loadBalancerClient, String providerName, String requestMappingPath, Map<String,Object> map)throws Exception{
		ServiceInstance si = loadBalancerClient.choose(providerName); //ServiceInstance 封装了服务的基本信息，如 IP，端口
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(si.getHost()).append(":").append(si.getPort()).append("/").append(requestMappingPath);
		RestTemplate rt = new RestTemplate();		//springMVC RestTemplate
		ParameterizedTypeReference<Object> type = new ParameterizedTypeReference<Object>(){};
        HttpHeaders headers = new HttpHeaders();	//请求头
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
	    HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<Map<String,Object>>(map, headers);
		ResponseEntity<Object> response = rt.exchange(sb.toString(),HttpMethod.POST, httpEntity, type);
		return response.getBody();
	}
	
	/**获取微服务地址
	 * @param loadBalancerClient
	 * @param providerName	//服务名称
	 * @return
	 */
	public static String getWsHost(LoadBalancerClient loadBalancerClient, String providerName) {
		ServiceInstance si = loadBalancerClient.choose(providerName); //ServiceInstance 封装了服务的基本信息，如 IP，端口
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(si.getHost()).append(":").append(si.getPort()).append("/");
		return sb.toString();
	}

}
