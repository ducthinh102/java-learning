package com.redsun.server.wh.authorize;

import java.io.Serializable;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.Value;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator{
	
	public static final Logger logger = LoggerFactory.getLogger(CustomPermissionEvaluator.class);
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

		logger.info("check permission '{}' for user '{}' for target '{}'", permission, authentication.getName(),
				targetDomainObject);

		/*if ("owner".equals(permission)) {
			Menu order = (Menu) targetDomainObject;
			if (order.getSortorder() < 500) {
				return hasRole("ROLE_ADMIN", authentication);
			}
		}*/

		return true;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return hasPermission(authentication, new DomainObjectReference(targetId, targetType), permission);
	}

	private boolean hasRole(String role, Authentication auth) {

		if (auth == null || auth.getPrincipal() == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		if (CollectionUtils.isEmpty(authorities)) {
			return false;
		}

		return authorities.stream().filter(ga -> role.equals(ga.getAuthority())).findAny().isPresent();
	}

	@Value
	static class DomainObjectReference {
		private Serializable targetId;
		private String targetType;
		public DomainObjectReference(Serializable targetId, String targetType) {
			this.targetId = targetId;
			this.targetType = targetType;
		}
	}
}
