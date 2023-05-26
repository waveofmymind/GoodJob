package com.goodjob.global.error.exception;

import com.goodjob.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

        public EntityNotFoundException(ErrorCode errorCode) {
            super(errorCode);
        }
}
