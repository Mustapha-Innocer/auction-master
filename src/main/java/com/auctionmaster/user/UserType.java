package com.auctionmaster.user;

import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum UserType {
	ADMIN,
	USER;

	public Set<SimpleGrantedAuthority> getAuthorities() {
		return Sets.newHashSet(new SimpleGrantedAuthority("ROLE_" + this.name()));
	}
}
