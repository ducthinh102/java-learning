package com.redsun.server.wh.common;

public class ServiceTransactionException extends Exception {

	private static final long serialVersionUID = 8987581643178548266L;
	
	private Integer id;
	private String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
