package com.goodjob.common.error.exception;


import com.goodjob.common.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

        public EntityNotFoundException(ErrorCode errorCode) {
            super(errorCode);
        }
}
