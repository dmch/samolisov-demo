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
package org.rcpbook.hyperbola.ui;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipselabs.p2.rcpupdate.utils.P2Util;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.rcpbook.hyperbola.ui.editors.ChatEditor;
import org.rcpbook.hyperbola.ui.editors.ChatEditorInput;
import org.rcpbook.hyperbola.ui.model.Session;
import org.rcpbook.hyperbola.ui.views.DebugConsole;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "org.rcpbook.hyperbola.ui.perspective"; //$NON-NLS-1$

	@Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(true);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[]{new DebugConsole()});
	}	
	
    @Override
	public void preStartup() {    	
    	hookIncomingChatListener();
    	checkForUpdates();
	}
    
    /**
     * Check installed update sites for updates
     */
    private void checkForUpdates() {
    	P2Util.checkForUpdates();
    }

	/**
     * Hooks a packet listener for incoming messages, to start a corresponding
     * chat if needed.
     */
    private void hookIncomingChatListener() {
        XMPPConnection connection = Session.getInstance().getConnection();
        if (connection != null) {
            PacketListener listener = new PacketListener() {
                public void processPacket(Packet packet) {
                    final Message message = (Message) packet;
                    if (message.getType() != Message.Type.chat)
                        return;
                    startChat(message);
                }
            };
            PacketFilter filter = new PacketTypeFilter(Message.class);
            connection.addPacketListener(listener, filter);
        }
    }

    /**
     * Starts a chat, if one hasn't already been started, triggered by an
     * incoming message.
     */
    private void startChat(final Message message) {
        String user = StringUtils.parseBareAddress(message.getFrom());
        Chat chat = Session.getInstance().getChat(user, false);
        // return if the chat is already established
        if (chat != null)
            return;
        IWorkbench workbench = getWorkbenchConfigurer().getWorkbench();
        workbench.getDisplay().asyncExec(new Runnable() {
            public void run() {
                if (PlatformUI.isWorkbenchRunning())
                    openChatEditor(message);
            }
        });
    }

    /**
     * Opens a chat editor triggered by the given message. Runs in the UI
     * thread.
     */
    private void openChatEditor(Message message) {
        IWorkbenchPage page = findPageForSession(Session.getInstance());
        if (page != null) {
            String user = message.getFrom();
            ChatEditorInput editorInput = new ChatEditorInput(user);
            try {
                IEditorPart editor = page.openEditor(editorInput, ChatEditor.ID);
                if (editor instanceof ChatEditor) {
                    ChatEditor chatEditor = (ChatEditor) editor;
                    chatEditor.processFirstMessage(message);
                }
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the first workbench page having the given session as its input.
     * 
     * @param session the session
     * @return the matching page, or <code>null</code>
     */
    private IWorkbenchPage findPageForSession(Session session) {
        IWorkbench workbench = getWorkbenchConfigurer().getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window != null)
            return window.getActivePage();
        return null;
    }
}
