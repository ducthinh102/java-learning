
package com.redsun.server.wh.validator;

import com.redsun.server.wh.model.Request ;
import org.springframework.validation.Errors;
import com.redsun.server.wh.common.MessagesKeys;
import org.springframework.validation.ValidationUtils;

public class RequestValidator extends AbstractValidator<Request>{

    @Override
    public void validateEntity(Request entity, Errors errors) {


    }
}