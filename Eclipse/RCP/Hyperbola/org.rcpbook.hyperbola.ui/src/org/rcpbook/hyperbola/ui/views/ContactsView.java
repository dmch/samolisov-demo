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
package org.rcpbook.hyperbola.ui.views;

import java.util.Collection;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.rcpbook.hyperbola.ui.HyperbolaContentProvider;
import org.rcpbook.hyperbola.ui.HyperbolaLableProvider;
import org.rcpbook.hyperbola.ui.model.Session;

public class ContactsView extends ViewPart {

	public static final String ID = "org.rcpbook.hyperbola.ui.views.contacts"; //$NON-NLS-1$		
	
	private TreeViewer viewer;
		
	@Override
	public void createPartControl(Composite parent) {				
		viewer = new TreeViewer(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		getSite().setSelectionProvider(viewer);	
		viewer.setLabelProvider(new HyperbolaLableProvider());
		viewer.setContentProvider(new HyperbolaContentProvider());
	
		Roster roster = Session.getInstance().getConnection().getRoster();
		viewer.setInput(roster);
		
		if (roster != null)
		{
			roster.addRosterListener(new RosterListener() {
	            public void entriesAdded(Collection args) {					
	                refresh();
	            }
				
	            public void entriesDeleted(Collection args) {
	                refresh();
	            }
				
	            public void entriesUpdated(Collection args) {					
	                refresh();
	            }
	            
				public void presenceChanged(Presence args) {					
	                refresh();
	            };
			});
		}
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getTree(), "org.rcpbook.hyperbola.ui.contactsView");
	}

	private void refresh() {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				viewer.refresh();
	        }
	    });
	}
		
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
