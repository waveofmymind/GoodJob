package com.goodjob.core.global.error.exception;


import com.goodjob.core.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

        public EntityNotFoundException(ErrorCode errorCode) {
            super(errorCode);
        }
}
