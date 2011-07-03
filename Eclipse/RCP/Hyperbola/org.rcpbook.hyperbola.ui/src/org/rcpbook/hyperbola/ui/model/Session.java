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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.RosterPacket;

public class Session implements IAdaptable {
    
    private static final String XMPP_PATH_DELEMITER = "@";
    
    private static final String GOOGLE_TALK_HOST = "talk.google.com";
    
    private static final String GOOGLE_SERVICE_NAME = "gmail.com";
    
    private static final String[] GOOGLE_SERVICE_NAMES = new String[]{GOOGLE_SERVICE_NAME, 
        "talk.google.com", "googlemail.com"};
	
	private ConnectionDetails connectionDetails;
	
	private XMPPConnection connection;
	
	private Map<String, Chat> chats = new HashMap<String, Chat>();
	
	private static Session INSTANCE;
	
	public static Session getInstance() {
		if (INSTANCE == null) 
			INSTANCE = new Session();
		return INSTANCE;
	}
	
	private Session() {}
	
	public XMPPConnection getConnection() {
		return connection;
	}
	
	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}
	
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	public ConnectionDetails getConnectionDetails() {
		return connectionDetails;
	}
	
	public void setConnectionDetails(ConnectionDetails connectionDetails) {
		this.connectionDetails = connectionDetails;
	}
	
	public void connectAndLogin(final IProgressMonitor monitor) throws XMPPException {
        PacketListener progressPacketListener = new PacketListener() {
            public void processPacket(Packet packet) {
                if (monitor.isCanceled())
                    throw new OperationCanceledException();
                String message = null;
                if (packet instanceof Authentication)
                    message = "Authenticating...";
                if (packet instanceof RosterPacket)
                    message = "Receiving roster...";
                if (message != null)
                    monitor.subTask(message);
            }
        };
        try {
            monitor.beginTask("Connecting...", IProgressMonitor.UNKNOWN);
            monitor.subTask("Contacting " + connectionDetails.getServer() + "...");
            
            // XMPPConnection.DEBUG_ENABLED = true;
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                        
            connection = new XMPPConnection(makeConnectionConfiguration(connectionDetails));
            connection.connect();
            connection.addPacketWriterListener(progressPacketListener,
                    new OrFilter(new PacketTypeFilter(Authentication.class), new PacketTypeFilter(RosterPacket.class)));
            connection.login(connectionDetails.getUserId(), connectionDetails.getPassword(), 
            		connectionDetails.getResource());
        } finally {
            if (connection != null)
                connection.removePacketWriterListener(progressPacketListener);
            monitor.done();
        }    
	}
	
	private ConnectionConfiguration makeConnectionConfiguration(ConnectionDetails connectionDetails) 
			throws XMPPException {		
		String[] userData = parseUserName(connectionDetails.getUserId());
		String serviceName = connectionDetails.getServer() == null ? userData[1] : connectionDetails.getServer();
		if (isGoogle(serviceName))
			return new ConnectionConfiguration(GOOGLE_TALK_HOST, connectionDetails.getPort(), GOOGLE_SERVICE_NAME);				
		else
			return new ConnectionConfiguration(serviceName, connectionDetails.getPort());			
	}
	
	private boolean isGoogle(String serviceName) {
		return Arrays.asList(GOOGLE_SERVICE_NAMES).contains(serviceName);
	}
	
	private String[] parseUserName(String userName) throws XMPPException {
		if (userName == null)
			throw new XMPPException("UserName must be not empty");
		
		int index = userName.lastIndexOf(XMPP_PATH_DELEMITER);
		if (index == -1)
			throw new XMPPException("UserName has invalid format. You should use USER@SERVER format");
		
		return new String[]{userName.substring(0, index), userName.substring(index + 1)};		
	}
	
    public Chat getChat(String participant, boolean create) {
        synchronized (chats) {
            Chat chat = (Chat) chats.get(participant);
            if (chat == null && create && connection != null) {
                chat = connection.getChatManager().createChat(participant, null);
                chats.put(participant, chat);
            }
            return chat;
        }
    }

    public void terminateChat(Chat chat) {
        chats.remove(chat.getParticipant());
    }
}
