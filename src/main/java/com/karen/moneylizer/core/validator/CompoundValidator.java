package com.karen.moneylizer.core.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public final class CompoundValidator implements Validator {

	private final Validator[] validators;
  
	public CompoundValidator(final Validator[] validators) {
		super();
		this.validators=validators;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		for (Validator v : validators) {
			if (v.supports(clazz)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		for (Validator v : validators) {
			if (v.supports(target.getClass())) {
				v.validate(target, errors);
			}
		}
	}

}
