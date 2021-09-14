package org.fh.controller.system;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.fh.util.Const;
import org.fh.util.Jurisdiction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 说明：main处理类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/main")
public class MainController {
	
	/**
	 * 用户注销
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/logout", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object logout() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		String USERNAME = Jurisdiction.getUsername();	//当前登录的用户名
		this.removeSession(USERNAME);					//清缓存
		Subject subject = SecurityUtils.getSubject(); 	//shiro销毁登录
		subject.logout();
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 清理session
	 */
	public void removeSession(String USERNAME){
		Session session = Jurisdiction.getSession();	//以下清除session缓存
		session.removeAttribute(Const.SESSION_USER);
		session.removeAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS);
		session.removeAttribute(USERNAME + Const.SESSION_ALLMENU);
		session.removeAttribute(USERNAME + Const.SHIROSET);
		session.removeAttribute(Const.SESSION_USERNAME);
		session.removeAttribute(Const.SESSION_U_NAME);
		session.removeAttribute(Const.SESSION_USERROL);
		session.removeAttribute(Const.SESSION_RNUMBERS);
		session.removeAttribute(Const.SKIN);
	}

}
