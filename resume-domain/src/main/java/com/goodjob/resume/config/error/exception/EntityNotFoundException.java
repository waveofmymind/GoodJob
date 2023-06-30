package com.goodjob.resume.config.error.exception;


import com.goodjob.resume.config.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

        public EntityNotFoundException(ErrorCode errorCode) {
            super(errorCode);
        }
}
