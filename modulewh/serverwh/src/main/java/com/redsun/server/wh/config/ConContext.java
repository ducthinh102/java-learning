package com.redsun.server.wh.config;

public class ConContext {

	private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

	public static void setConId(String conId) {
		CONTEXT.set(conId);
	}

	public static String getConId() {
		return CONTEXT.get();
	}

}
