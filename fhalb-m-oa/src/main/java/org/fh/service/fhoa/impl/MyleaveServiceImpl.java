package org.fh.service.fhoa.impl;

import java.util.List;
import java.util.Map;

import org.fh.entity.Page;
import org.fh.entity.PageData;
import org.fh.mapper.dsno1.fhoa.MyleaveMapper;
import org.fh.service.fhoa.MyleaveService;
import org.fh.util.LoadBalancerUtil;
import org.fh.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 说明： 请假申请服务接口实现类
 * 创建人：FH Q313596790
 * 官网：www.fhadmin.org
 */
@Service(value="myleaveServiceImpl")
@Transactional //开启事物
public class MyleaveServiceImpl implements MyleaveService {
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;//LoadBalancer负载均衡器
	
	@Autowired
	private MyleaveMapper myleaveMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd, Map<String,Object> map)throws Exception{
		myleaveMapper.save(pd);
		map.put("tokenKey", Tools.creatTokenKey("startProByKey"));
		map.put("processKey", "KEY_leave");
		LoadBalancerUtil.responseByPost(this.loadBalancerClient, "fh-flowable", "startPro/byKey", map);	//请求工作流微服务启动流程实例接口(启动流程实例(请假单流程)通过KEY)
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		myleaveMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		myleaveMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return myleaveMapper.datalistPage(page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return myleaveMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		myleaveMapper.deleteAll(ArrayDATA_IDS);
	}

}
