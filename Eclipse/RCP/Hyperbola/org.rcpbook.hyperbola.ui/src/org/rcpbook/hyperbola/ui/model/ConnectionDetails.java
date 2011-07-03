/*******************************************************************************
 * Copyright (c) 2004 - 2011 Jean-Michel Lemieux, Jeff McAffer and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Hyperbola is an RCP application developed for the book 
 *     Eclipse Rich Client Platform - 
 *         Designing, Coding, and Packaging Java Applications 
 * See http://eclipsercp.org
 * 
 * Contributors:
 *     Jean-Michel Lemieux and Jeff McAffer - initial implementation
 *     Pavel Samolisov - building and additional features
 *******************************************************************************/
package org.rcpbook.hyperbola.ui.model;

public class ConnectionDetails {
	
    private static final int XMPP_DEFAULT_PORT = 5222;
	
    private String userId;
	
	private String server;
	
	private String password;
	
	private int port;
	
	public ConnectionDetails(String userId, String password) {
		this(userId, null, XMPP_DEFAULT_PORT, password);
	}
	
	public ConnectionDetails(String userId, String server, String password) {
		this(userId, server, XMPP_DEFAULT_PORT, password);
	}
	
	public ConnectionDetails(String userId, String server, int port, String password) {
		this.userId = userId;
		this.server = server;
		this.port = port;
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public String getServer() {
		return server;
	}

	public int getPort() {
    	return port;
    }
	
	public String getPassword() {
		return password;
	}
	
    public String getResource() {
        return String.valueOf(System.currentTimeMillis());
    }    
}
