package org.fh.controller.system;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.fh.entity.PageData;
import org.fh.util.Const;
import org.fh.util.IniFileUtil;
import org.fh.util.Jurisdiction;
import org.fh.util.PathUtil;
import org.fh.util.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	/**同步到配置文件
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/saveSysSet")
	@ResponseBody
	public Object saveSysSet(@RequestBody PageData pd) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		if(Tools.checkTokenKey("sysSet", pd.getString("tokenKey"))) {	//检验tokenKey
			String infFilePath = PathUtil.getClasspath()+Const.SYSSET;		//配置文件路径
			String sysName = pd.getString("sysName");						//系统名称
			String onlineIp = pd.getString("onlineIp");						//在线管理IP
			String onlinePort = pd.getString("onlinePort");					//在线管理端口
			String fhsmsSound = pd.getString("fhsmsSound");					//信息提示音
			String imIp = pd.getString("imIp");								//即时聊天IP
			String PORT = pd.getString("PORT");								//邮箱服务器端口
			/*写入配置文件*/
			IniFileUtil.writeCfgValue(infFilePath, "SysSet1", Const.SYSNAME, sysName);		
			IniFileUtil.writeCfgValue(infFilePath, "SysSet1", "onlineIp", onlineIp);
			IniFileUtil.writeCfgValue(infFilePath, "SysSet1", "onlinePort", onlinePort);
			IniFileUtil.writeCfgValue(infFilePath, "SysSet1", "imIp", imIp);
			IniFileUtil.writeCfgValue(infFilePath, "SysSet1", "fhsmsSound", fhsmsSound);
			IniFileUtil.writeCfgValue(infFilePath, "SysSet1", "PORT", PORT);						
		}else {
			errInfo = "errer";
		}
		map.put("result", errInfo);
		return map;
	}
	
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
