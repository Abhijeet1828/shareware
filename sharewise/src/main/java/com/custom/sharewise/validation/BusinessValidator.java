package com.custom.sharewise.validation;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;

public interface BusinessValidator {

	void validate(Object value) throws CommonException, UnauthorizedException;

}
