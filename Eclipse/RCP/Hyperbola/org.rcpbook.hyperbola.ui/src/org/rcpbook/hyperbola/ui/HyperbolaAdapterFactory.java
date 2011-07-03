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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.Presence;
import org.rcpbook.hyperbola.ui.model.Session;

public class HyperbolaAdapterFactory implements IAdapterFactory {

	private static final Class[] TYPES = new Class[]{IWorkbenchAdapter.class};
	
	private IWorkbenchAdapter groupAdapter = new IWorkbenchAdapter() {				
		private int getAvailableCount(RosterGroup group) {
			int result = 0;
			for (RosterEntry contact : group.getEntries()) {
				Presence presence = getPresence(contact);
				if (presence != null && presence.getMode() != Presence.Mode.xa) {
					result ++;
				}
			}
			
			return result;
		}
				
		public Object getParent(Object o) {
			return Session.getInstance().getConnection().getRoster();
		}		
				
		public String getLabel(Object o) {
			RosterGroup group = (RosterGroup) o;
			return group.getName() + " (" + getAvailableCount(group) + "/" + group.getEntryCount() + ")";
		}
				
		public ImageDescriptor getImageDescriptor(Object object) {
			return AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.GROUP);
		}
				
		public Object[] getChildren(Object o) {
			RosterGroup group = (RosterGroup) o;
			return new ArrayList<RosterEntry>(group.getEntries()).toArray(new RosterEntry[]{});
		}
	};
	
	private IWorkbenchAdapter entryAdapter = new IWorkbenchAdapter() {				
		public Object getParent(Object o) {
			return null;
		}
		
		public String getLabel(Object o) {
			RosterEntry entry = (RosterEntry) o;
			return entry.getName() == null || "".equals(entry.getName()) ? entry.getUser() 
					: entry.getName() + " (" + entry.getUser() + ")";
		}
				
		public ImageDescriptor getImageDescriptor(Object object) {
			RosterEntry entry = (RosterEntry) object;
			return AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, presenceToKey(getPresence(entry)));			
		}
				
		public Object[] getChildren(Object o) {
			return new Object[0];
		}
	};
	
	private IWorkbenchAdapter rosterAdapter = new IWorkbenchAdapter() {				
		public Object getParent(Object o) {		
			return null;
		}
		
		public String getLabel(Object o) {
			return "";
		}
				
		public ImageDescriptor getImageDescriptor(Object object) {
			return null;
		}
				
		public Object[] getChildren(Object o) {
			Roster roster = (Roster) o;
			List<Object> result = new ArrayList<Object>();
			result.addAll(roster.getGroups());
			result.addAll(roster.getUnfiledEntries());
			return result.toArray();
		}
	};
	
	private Presence getPresence(RosterEntry contact) {
		return Session.getInstance().getConnection().getRoster().getPresence(contact.getUser());
	}
	
	private String presenceToKey(Presence presence) {
		if (presence == null)
			return IImageKeys.OFFLINE;
		
		Presence.Mode mode = presence.getMode();
		Presence.Type type = presence.getType();
		
		// If type is unavailable then we're unavailable
		if (type == Presence.Type.available) {
			// if type and mode are both 'available' or mode is null then we're actually
			// available
			if (mode == null || mode == Presence.Mode.available)
				return IImageKeys.ONLINE;
			// If mode is away then we're away
			else if (mode == Presence.Mode.away || mode == Presence.Mode.xa)
				return IImageKeys.AWAY;
			else if (mode == Presence.Mode.chat)
				return IImageKeys.CHAT;		
			else if (mode == Presence.Mode.dnd)
				return IImageKeys.DO_NOT_DISTURB;			
		}
						
		return IImageKeys.OFFLINE;
	}	
		
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof RosterGroup)
			return groupAdapter;
		if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof RosterEntry)
			return entryAdapter;
		if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof Roster)
			return rosterAdapter;
		return null;
	}
	
	public Class[] getAdapterList() {
		return TYPES;
	}
}
