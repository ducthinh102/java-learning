
package com.redsun.server.main.controller.common;

public class ServerException extends RuntimeException {
	private static final long serialVersionUID = -506575992842605166L;
	
	private Integer code;
    private Object data;

    public ServerException(final Integer code) {
        this.code = code;
    }

    public ServerException(final Integer code, final Object data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(final Object data) {
        this.data = data;
    }
}
