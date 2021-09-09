
package com.redsun.server.main.validator;

import com.redsun.server.main.model.User ;
import org.springframework.validation.Errors;
import com.redsun.server.main.common.MessagesKeys;
import org.springframework.validation.ValidationUtils;

public class UserValidator extends AbstractValidator<User>{

    @Override
    public void validateEntity(User entity, Errors errors) {


    }
}