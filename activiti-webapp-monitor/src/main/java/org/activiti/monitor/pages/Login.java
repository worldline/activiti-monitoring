package org.activiti.monitor.pages;

import java.io.IOException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.tynamo.security.services.SecurityService;

public class Login {
	@Inject
	private Logger logger;

	@Property
	private String jsecLogin;

	@Property
	private String jsecPassword;

	@Property
	private boolean jsecRememberMe;

	@Persist(PersistenceConstants.FLASH)
	private String loginMessage;

	@Inject
	private Response response;

	@Inject
	private RequestGlobals requestGlobals;

	@Inject
	private SecurityService securityService;

	public Object onActionFromJsecLoginForm() {

		Subject currentUser = securityService.getSubject();

		if (currentUser == null) {
			throw new IllegalStateException("Subject can`t be null");
		}

		UsernamePasswordToken token = new UsernamePasswordToken(jsecLogin,
				jsecPassword);
		token.setRememberMe(jsecRememberMe);

		try {
			currentUser.login(token);
		} catch (UnknownAccountException e) {
			loginMessage = "Account not exists";
			return null;
		} catch (IncorrectCredentialsException e) {
			loginMessage = "Wrong password";
			return null;
		} catch (LockedAccountException e) {
			loginMessage = "Account locked";
			return null;
		} catch (AuthenticationException e) {
			loginMessage = "Authentication Error";
			return null;
		}

		SavedRequest savedRequest = WebUtils
				.getAndClearSavedRequest(requestGlobals.getHTTPServletRequest());

		// TODO: try using shiro's own WebUtils.redirectToSavedRequest
		if (savedRequest != null
				&& savedRequest.getMethod().equalsIgnoreCase("GET")) {
			try {
				response.sendRedirect(savedRequest.getRequestUrl());
				return null;
			} catch (IOException e) {
				logger.warn("Can't redirect to saved request.");
				return Index.class;
			}
		} else {
			return Index.class;
		}

	}

	public String getLoginMessage() {
		if (hasLoginMessage()) {
			return loginMessage;
		} else {
			return " ";
		}
	}

	public boolean hasLoginMessage() {
		return StringUtils.hasText(loginMessage);
	}
}