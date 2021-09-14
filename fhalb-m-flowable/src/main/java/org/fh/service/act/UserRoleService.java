package org.fh.service.act;

import java.util.List;

import org.fh.entity.PageData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/** 
 * 说明： 用户和角色接口
 * 创建人：FH Q313596790
 * 官网：www.fhadmin.org
 */
@FeignClient(name="fh-system") //Feign 客户端 ， fh-system 为服务名称
public interface UserRoleService {
	
	/**用户列表
	 * @param pd
	 * @return
	 */
	@RequestMapping(value="/user/listUsersByMs")
	public List<PageData> listUsersByMs(@RequestBody PageData pd);
	
	/**角色列表
	 * @param pd
	 * @return
	 */
	@RequestMapping(value="/role/listRolesByMs")
	public List<PageData> listRolesByMs(@RequestBody PageData pd);

}
