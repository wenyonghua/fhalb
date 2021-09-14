package org.fh.service.system;

import org.fh.entity.PageData;

/**
 * 说明：用户服务接口
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
public interface UsersService {
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveUser(PageData pd)throws Exception;
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editUser(PageData pd)throws Exception;
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteUser(PageData pd)throws Exception;
	
	/**批量删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteAllUser(String[] USER_IDS)throws Exception;
	
}
