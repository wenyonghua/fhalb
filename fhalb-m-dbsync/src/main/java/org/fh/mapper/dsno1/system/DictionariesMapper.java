package org.fh.mapper.dsno1.system;

import org.fh.entity.PageData;

/**
 * 说明：数据字典Mapper
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
public interface DictionariesMapper {
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
}
