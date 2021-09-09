package com.redsun.server.wh.common;

public enum Role {
	
	ADMIN(1),
	USER(2),
	EXTERNAL(3),
	OWNER(4),
	DIRECTOR(5),
	SUPPLIER(11),
	MATERIAL(14),
	MATERIAL_CODE(15),
	STORE(16);
	
	private Integer id;
	Role(Integer id){
		this.id = id;
	}
	
	public Integer getId(){
		return id;
	}
	
	public static String getRole(Integer id){
		if(id == Role.ADMIN.getId()){
			return "ADMIN";
		}
		if(id == Role.USER.getId()){
			return "USER";
		}
		if(id == Role.EXTERNAL.getId()){
			return "EXTERNAL";
		}
		if(id == Role.OWNER.getId()){
			return "OWNER";
		}
		if(id == Role.SUPPLIER.getId()){
			return "SUPPLIER";
		}
		if(id == Role.DIRECTOR.getId()){
			return "DIRECTOR";
		}
		if(id == Role.MATERIAL.getId()){
			return "MATERIAL";
		}
		if(id == Role.MATERIAL_CODE.getId()){
			return "MATERIAL_CODE";
		}
		if(id == Role.STORE.getId()){
			return "STORE";
		}
		return "";
	}
}
