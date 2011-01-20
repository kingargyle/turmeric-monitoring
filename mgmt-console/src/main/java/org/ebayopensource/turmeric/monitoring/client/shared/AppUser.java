/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.ebayopensource.turmeric.monitoring.client.model.AuthenticationState;
import org.ebayopensource.turmeric.monitoring.client.model.UserAction;

public class AppUser implements Serializable {

	private static final long serialVersionUID = 8423602941996307213L;
	
	private String username;
	private String token;
	private String password;
	private String domain;
	
	private AuthenticationState authenticationState;
	private Collection<AppUserRole> roles;
	private static AppUser user;
	
	public static AppUser getUser () {
	    return user;
	}
	
	
	
	public static void logout () {
	    user = null;
	}
	
	
	public static AppUser fromCookie (String cookie) {
	    if (user != null)
	        logout();
	    if (cookie == null)
	        return null;

	    //parse cookie
	    String[] split = cookie.split("\\|");

	    if (split == null)
	        return null;
	    if (split.length < 3)
	        return null;

	   return newAppUser(split[0], split[1], split[2]);
	}
	
	public static String toCookie () {
	    if (user == null)
	        return null;
	    
	    return user.getUsername()+"|"+user.getPassword() +"|"+user.getDomain();
	}
	
	public static AppUser newAppUser (String login, String credential, String domain) {
	    if (user != null)
	        logout();
	    
	    user = new AppUser();
	    user.setUsername(login);
	    user.setPassword(credential);
	    user.setDomain(domain);
	    return user;
	}
	
	
	private AppUser() {
		authenticationState = AuthenticationState.Unauthenticated;
		roles = Collections.emptyList();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setPassword(String pwd) {
	    this.password = pwd;
	}
	
	public String getPassword() {
	    return this.password;
	}
	
	public AuthenticationState getAuthenticationState() {
		return authenticationState;
	}
	public void setAuthenticationState(AuthenticationState authenticationState) {
		this.authenticationState = authenticationState;
	}
	public Collection<AppUserRole> getRoles() {
		return roles;
	}
	public void setRoles(Collection<AppUserRole> roles) {
		this.roles = roles;
	}

	public void setDomain(String domain) {
        this.domain = domain;
    }



    public String getDomain() {
        return domain;
    }



    @Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("username {").append(username).append("}");
		buffer.append(" token {").append(token).append("}");
		buffer.append(" authenticationState {").append(authenticationState).append("}");
		if (roles != null && roles.size() > 0) {
			for (AppUserRole role : roles) {
				buffer.append(" role {").append(role).append("}");
			}
		}
		
		return buffer.toString();
	}
}
