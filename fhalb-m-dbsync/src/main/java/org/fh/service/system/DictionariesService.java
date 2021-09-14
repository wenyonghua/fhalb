package org.fh.service.system;

import org.fh.entity.PageData;

/**
 * 说明：数据字典服务接口
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
public interface DictionariesService {
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;

}
