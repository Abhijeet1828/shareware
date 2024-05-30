package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;

public interface SettleService {

	Object settleSimplify(Long groupId) throws CommonException;

}
