
package com.redsun.server.wh.validator;

import com.redsun.server.wh.model.User ;
import org.springframework.validation.Errors;
import com.redsun.server.wh.common.MessagesKeys;
import org.springframework.validation.ValidationUtils;

public class UserValidator extends AbstractValidator<User>{

    @Override
    public void validateEntity(User entity, Errors errors) {


    }
}