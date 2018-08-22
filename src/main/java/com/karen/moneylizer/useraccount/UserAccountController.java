package com.karen.moneylizer.useraccount;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody UserAccountEntity account,
			HttpServletResponse response) throws IOException {
		return userAccountService.authenticateUserAndSetResponsenHeader(
				account.getUsername(), account.getPassword(), response);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String create(@RequestBody UserAccountEntity userAccount,
			HttpServletResponse response, BindingResult result) {
		String username = userAccount.getUsername();
		String password = userAccount.getPassword();
		// createUserDetailsDtoValidator.validate(userDetailsDTO, result);
		// if (result.hasErrors()) {
		// throw new
		// InputValidationException(result.getFieldError().getField());
		// }
		userAccountService.saveIfNotExists(username, password);
		return userAccountService.authenticateUserAndSetResponsenHeader(
				username, password, response);
	}
}
