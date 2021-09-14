package org.fh.service.system.impl;

import java.util.List;

import org.fh.entity.Page;
import org.fh.entity.PageData;
import org.fh.entity.system.Dictionaries;
import org.fh.mapper.dsno1.system.DictionariesMapper;
import org.fh.service.system.DictionariesService;
import org.fh.util.LoadBalancerUtil;
import org.fh.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：数据字典服务接口实现类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Service
@Transactional //开启事物
public class DictionariesServiceImpl implements DictionariesService {
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;//LoadBalancer负载均衡器
	
	@Autowired
	private DictionariesMapper dictionariesMapper;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDict(String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		for(Dictionaries dict : dictList){
			dict.setTreeurl("dictionaries_list.html?DICTIONARIES_ID="+dict.getDICTIONARIES_ID());
			dict.setSubDict(this.listAllDict(dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listSubDictByParentId(String parentId) throws Exception {
		return dictionariesMapper.listSubDictByParentId(parentId);
	}
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)用于代码生成器引用数据字典
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDictToCreateCode(String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		for(Dictionaries dict : dictList){
			dict.setTreeurl("setDID('"+dict.getDICTIONARIES_ID()+"');");
			dict.setSubDict(this.listAllDictToCreateCode(dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return dictionariesMapper.datalistPage(page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return dictionariesMapper.findById(pd);
	}
	
	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return dictionariesMapper.findByBianma(pd);
	}

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dictionariesMapper.save(pd);
		pd.put("tokenKey", Tools.creatTokenKey("dictionariesAdd"));
		LoadBalancerUtil.responseByPost(this.loadBalancerClient, "fh-dbsync", "dictionaries/add", pd);	//请求数据库表同步微服务
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dictionariesMapper.edit(pd);
		pd.put("tokenKey", Tools.creatTokenKey("dictionariesEdit"));
		LoadBalancerUtil.responseByPost(this.loadBalancerClient, "fh-dbsync", "dictionaries/edit", pd);	//请求数据库表同步微服务
	}
	
	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception{
		return dictionariesMapper.findFromTbs(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dictionariesMapper.delete(pd);
		pd.put("tokenKey", Tools.creatTokenKey("dictionariesDel"));
		LoadBalancerUtil.responseByPost(this.loadBalancerClient, "fh-dbsync", "dictionaries/delete", pd);	//请求数据库表同步微服务
	}
	
}
