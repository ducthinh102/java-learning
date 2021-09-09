package com.redsun.server.main.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class ConIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

	private static String DEFAULT_CON_ID = "0";

	@Override
	public String resolveCurrentTenantIdentifier() {
		String currentConId = ConContext.getConId();
		return (currentConId != null) ? currentConId : DEFAULT_CON_ID;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
