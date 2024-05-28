package com.custom.sharewise.validation;

import com.custom.common.utilities.exception.CommonException;

public interface BusinessValidator {

	void validate(Object value) throws CommonException;

}
