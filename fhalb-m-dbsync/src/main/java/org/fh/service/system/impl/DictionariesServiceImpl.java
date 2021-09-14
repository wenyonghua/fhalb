package org.fh.service.system.impl;

import org.fh.entity.PageData;
import org.fh.mapper.dsno1.system.DictionariesMapper;
import org.fh.service.system.DictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：按钮权限服务接口实现类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Service
@Transactional //开启事物
public class DictionariesServiceImpl implements DictionariesService {
	
	@Autowired
	private DictionariesMapper dictionariesMapper;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dictionariesMapper.save(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dictionariesMapper.edit(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dictionariesMapper.delete(pd);
	}
	
}
