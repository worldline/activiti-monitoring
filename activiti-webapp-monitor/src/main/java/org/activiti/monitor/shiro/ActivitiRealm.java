package org.activiti.monitor.shiro;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.GroupQuery;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ActivitiRealm extends AuthorizingRealm {
	final static String ADMIN_ROLE = "admin";
	@Inject
	ProcessEngine processEngine;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		if (principals == null)
			throw new AuthorizationException(
					"PrincipalCollection was null, which should not happen");

		if (principals.isEmpty())
			return null;

		if (principals.fromRealm(getName()).size() <= 0)
			return null;

		String username = (String) principals.fromRealm(getName()).iterator()
				.next();

		if (username == null)
			return null;

		return new SimpleAuthorizationInfo();
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String username = usernamePasswordToken.getUsername();
		char[] pswrd = usernamePasswordToken.getPassword();
		String password = String.copyValueOf(pswrd);

		// check if the username and password are correct
		IdentityService identityService = processEngine.getIdentityService();
		if (!identityService.checkPassword(username, password)) {
			throw new IncorrectCredentialsException();
		}

		// check if the username ins member of "admin" role
		GroupQuery query = identityService.createGroupQuery();
		System.out.println(query.groupMember(username).groupId(ADMIN_ROLE)
				.count());
		if (query.groupMember(username).groupId(ADMIN_ROLE).count() == 0) {
			throw new IncorrectCredentialsException();
		}
		return buildAuthenticationInfo(username, password);
	}

	private AuthenticationInfo buildAuthenticationInfo(String userId,
			String password) {
		return new SimpleAuthenticationInfo(userId, password, getName());
	}

}
