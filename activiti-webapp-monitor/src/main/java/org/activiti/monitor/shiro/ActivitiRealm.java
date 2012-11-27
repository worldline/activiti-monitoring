package org.activiti.monitor.shiro;

import org.activiti.engine.ProcessEngine;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ActivitiRealm extends SimpleAccountRealm{
	
//	@Inject 	ProcessEngine processEngine;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		if (!accountExists(upToken.getUsername()))
		{
			throw new UnknownAccountException("Unknown account" + upToken.getUsername());
		}

		return super.doGetAuthenticationInfo(token);
		
	}
	
	

}
