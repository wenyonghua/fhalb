package org.fh.mapper.dsno2.system;

import org.fh.entity.PageData;

/**
 * 说明：用户Mapper
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
public interface UsersMapper {
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	void saveUser(PageData pd);
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	void editUser(PageData pd);
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	void deleteUser(PageData pd);
	
	/**批量删除用户
	 * @param pd
	 * @throws Exception
	 */
	void deleteAllUser(String[] USER_IDS);

}
