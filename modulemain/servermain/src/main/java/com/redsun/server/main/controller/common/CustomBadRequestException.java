package com.redsun.server.main.controller.common;

import org.springframework.http.HttpStatus;

public class CustomBadRequestException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3312796889822716917L;
	private HttpStatus code;
    private Object data;

    public CustomBadRequestException(HttpStatus code, Object data) {
        this.code = code;
        this.data = data;
    }

    public CustomBadRequestException(Object data) {
        this.data = data;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
