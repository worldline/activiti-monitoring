package org.activiti.monitor.pages;

import org.activiti.engine.IdentityService;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Login {

	@Persist
	@Property
	private String username;

	@Property
	private String password;

	@Component
	private Form loginForm;

	@InjectComponent(value = "password")
	private PasswordField passwordField;

	@Inject
	IdentityService identityService;

	void onValidateFromLoginForm() {
		System.out.println("Username=" + username);
		;
		System.out.println("Username=" + password);
		;

		if (!identityService.checkPassword(username, password)) {
			// record an error, and thereby prevent Tapestry from emitting a
			// "success" event
			loginForm.recordError(passwordField,
					"Invalid user name or password.");
		}
	}

	/**
	 * Validation passed, so we'll go to the "PostLogin" page
	 */
	Object onSuccess() {

		return Index.class;

		/*
		 * 
		 * UsernamePasswordToken token = new UsernamePasswordToken(username,
		 * password);
		 * 
		 * //�Remember Me� built-in: token.setRememberMe(true);
		 * 
		 * Subject currentUser = SecurityUtils.getSubject();
		 * 
		 * currentUser.login(token); return null;
		 */
	}

}
