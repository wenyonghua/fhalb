package org.fh.controller.system;

import java.util.HashMap;
import java.util.Map;

import org.fh.controller.base.BaseController;
import org.fh.service.system.UsersService;
import org.fh.util.Tools;
import org.fh.entity.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 说明：系统用户处理类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/user")
public class UsersController extends BaseController {
	
	@Autowired
    private UsersService usersService;
	
	/**保存用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object add(@RequestBody PageData pd) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		if(Tools.checkTokenKey("userAdd", pd.getString("tokenKey"))) {	//检验tokenKey
			usersService.saveUser(pd);
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**
	 * 修改用户
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit(@RequestBody PageData pd) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		if(Tools.checkTokenKey("userEdit", pd.getString("tokenKey"))) {	//检验tokenKey
			usersService.editUser(pd);	//执行修改
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**删除用户
	 * @return
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete(@RequestBody PageData pd) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		if(Tools.checkTokenKey("userDel", pd.getString("tokenKey"))) {	//检验tokenKey
			usersService.deleteUser(pd);		//删除用户
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**
	 * 批量删除
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll(@RequestBody PageData pd) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		if(Tools.checkTokenKey("userDelAll", pd.getString("tokenKey"))) {	//检验tokenKey
			String SUSER_ID = pd.getString("SUSER_ID");
			String ArrayUSER_IDS[] = SUSER_ID.split(",");
			usersService.deleteAllUser(ArrayUSER_IDS);	//删除用户
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);						//返回结果
		return map;
	}
	
}
