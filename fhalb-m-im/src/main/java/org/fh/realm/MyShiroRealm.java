package org.fh.realm;

import java.util.Collection;
import java.util.HashSet;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.fh.util.Const;
import org.fh.util.Jurisdiction;
import org.fh.entity.PageData;
 
/**
 * 说明：Shiro身份认证
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
public class MyShiroRealm extends AuthorizingRealm {
 
    /**
     * 登录认证(只在system微服务端做登录认证，其它根据Redis判断认证状态)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken; 	//UsernamePasswordToken用于存放提交的登录信息
	    PageData pd = new PageData();
	    pd.put("USERNAME", token.getUsername());
			try {
			} catch (Exception e) {
				return null;
			}
		return null;
    }
    
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String USERNAME = (String) super.getAvailablePrincipal(principals);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Session session = Jurisdiction.getSession();
		Collection<String> shiroSet= new HashSet<String>();
		shiroSet = (Collection<String>)session.getAttribute(USERNAME + Const.SHIROSET);
		if(null != shiroSet){
			info.addStringPermissions(shiroSet);
			return info;
		}else {
			return null;
		}
	}

}
