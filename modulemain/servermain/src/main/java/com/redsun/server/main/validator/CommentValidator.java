
package com.redsun.server.main.validator;

import com.redsun.server.main.model.Comment ;
import org.springframework.validation.Errors;
import com.redsun.server.main.common.MessagesKeys;
import org.springframework.validation.ValidationUtils;

public class CommentValidator extends AbstractValidator<Comment>{

    @Override
    public void validateEntity(Comment entity, Errors errors) {


    }
}