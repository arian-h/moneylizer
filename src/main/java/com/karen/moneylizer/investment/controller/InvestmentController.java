package com.karen.moneylizer.investment.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class InvestmentController {

	/*TODO update return type and the write comments*/

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public void list(@PathVariable long investmentId, Principal principal) {
	}

	@RequestMapping(path = "/{investmentId}", method = RequestMethod.GET)
	public void get(@PathVariable long investmentId, Principal principal) {
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public void create(@PathVariable long investmentId, Principal principal) {
	}

	@RequestMapping(path = "/{investmentId}", method = RequestMethod.PUT)
	public void update(@PathVariable long investmentId, Principal principal) {
	}

	@RequestMapping(path = "/{investmentId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable long investmentId, Principal principal) {
	}

}
