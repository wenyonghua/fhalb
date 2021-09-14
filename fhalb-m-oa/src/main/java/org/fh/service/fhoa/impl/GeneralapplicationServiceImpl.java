package org.fh.service.fhoa.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.fh.entity.Page;
import org.fh.entity.PageData;
import org.fh.mapper.dsno1.fhoa.GeneralapplicationMapper;
import org.fh.service.fhoa.GeneralapplicationService;
import org.fh.util.LoadBalancerUtil;
import org.fh.util.Tools;

/** 
 * 说明： 通用申请接口实现类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 * @version
 */
@Service
@Transactional //开启事物
public class GeneralapplicationServiceImpl implements GeneralapplicationService{
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;//LoadBalancer负载均衡器

	@Autowired
	private GeneralapplicationMapper generalapplicationMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd,Map<String,Object> map,String KEY)throws Exception{
		generalapplicationMapper.save(pd);
		map.put("tokenKey", Tools.creatTokenKey("startProByKey"));
		map.put("processKey", KEY);
		LoadBalancerUtil.responseByPost(this.loadBalancerClient, "fh-flowable", "startPro/byKey", map);	//请求工作流微服务启动流程实例接口(启动流程实例通过KEY)
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		generalapplicationMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		generalapplicationMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return generalapplicationMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return generalapplicationMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return generalapplicationMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		generalapplicationMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

