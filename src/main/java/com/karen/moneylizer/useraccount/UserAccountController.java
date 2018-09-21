package com.karen.moneylizer.useraccount;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public interface UserAccountController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody UserAccountEntity account,
			HttpServletResponse response) throws IOException;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String create(@RequestBody UserAccountEntity userAccount,
			HttpServletResponse response, BindingResult result);

}