package org.fh.service.system.impl;

import org.fh.service.system.UsersService;

import org.fh.entity.PageData;
import org.fh.mapper.dsno2.system.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：用户服务接口实现类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Service
@Transactional //开启事物
public class UsersServiceImpl implements UsersService {
	
	@Autowired
	private UsersMapper usersMapper;
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveUser(PageData pd)throws Exception {
		usersMapper.saveUser(pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editUser(PageData pd)throws Exception{
		usersMapper.editUser(pd);
	}

	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteUser(PageData pd)throws Exception{
		usersMapper.deleteUser(pd);
	}
	
	/**批量删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteAllUser(String[] USER_IDS)throws Exception{
		usersMapper.deleteAllUser(USER_IDS);
	}
	
}
