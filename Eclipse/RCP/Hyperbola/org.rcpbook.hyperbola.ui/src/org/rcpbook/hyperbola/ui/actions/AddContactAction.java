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
package org.rcpbook.hyperbola.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;
import org.rcpbook.hyperbola.ui.Application;
import org.rcpbook.hyperbola.ui.IImageKeys;
import org.rcpbook.hyperbola.ui.dialogs.AddContactDialog;
import org.rcpbook.hyperbola.ui.model.Session;

public class AddContactAction extends Action implements ISelectionListener, IWorkbenchAction {

	public static final String ID = "org.rcpbook.hyperbola.addContact";
	
	private IWorkbenchWindow window;
	
	private IStructuredSelection selection;	
	
	public AddContactAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setActionDefinitionId(ID);
		setText("&Add Contact...");
		setToolTipText("Add a contact to your contact list");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.ADD_CONTACT));
		window.getSelectionService().addSelectionListener(this);
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
			setEnabled(this.selection.size() == 1 && this.selection.getFirstElement() instanceof RosterGroup);			
		} else {
			setEnabled(false);
		}
	}

	@Override
	public void run() {	
		AddContactDialog dialog = new AddContactDialog(window.getShell());
		int code = dialog.open();
		if (code == Window.OK) {
			Object item = selection.getFirstElement();
			RosterGroup group = (RosterGroup) item;
			try {
				Roster list = Session.getInstance().getConnection().getRoster();
				String user = dialog.getUserId() + "@" + dialog.getServer();
				String[] groups = new String[] {group.getName()};
				list.createEntry(user, dialog.getNickname(), groups);
			}
			catch (XMPPException e) {
				// TODO: handle exception
			}			
		}
	}	
}
