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

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.rcpbook.hyperbola.ui.actions.AddContactAction;
import org.rcpbook.hyperbola.ui.actions.ChatAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction addContactAction;
	private IWorkbenchAction chatAction;
	private IWorkbenchAction preferencesAction;
	private IWorkbenchAction helpAction;
	private IWorkbenchAction helpSearchAction;
	private IContributionItem perspectivesMenu;
	
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	exitAction = ActionFactory.QUIT.create(window);
    	register(exitAction);
    	aboutAction = ActionFactory.ABOUT.create(window);
    	register(aboutAction);
    	addContactAction = new AddContactAction(window);
    	register(addContactAction);
    	chatAction = new ChatAction(window);
    	register(chatAction);
    	preferencesAction = ActionFactory.PREFERENCES.create(window);
    	register(preferencesAction);
    	helpAction = ActionFactory.HELP_CONTENTS.create(window);
    	register(helpAction);
    	helpSearchAction = ActionFactory.HELP_SEARCH.create(window);
    	register(helpSearchAction);
    	perspectivesMenu = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);    	
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	MenuManager layoutMenu = new MenuManager("Switch layout", "layout");
    	layoutMenu.add(perspectivesMenu);
    	
    	MenuManager hyperbolaMenu = new MenuManager("&Hyperbola", "hyperbola");
    	hyperbolaMenu.add(addContactAction);
    	hyperbolaMenu.add(chatAction);
    	hyperbolaMenu.add(new Separator());
    	hyperbolaMenu.add(layoutMenu);
    	hyperbolaMenu.add(new Separator());
    	hyperbolaMenu.add(preferencesAction);
    	hyperbolaMenu.add(new Separator());
    	hyperbolaMenu.add(exitAction);    	    	
    	
    	MenuManager helpMenu = new MenuManager("&Help", "help");
    	helpMenu.add(helpAction);
    	helpMenu.add(helpSearchAction);
    	helpMenu.add(new Separator());
    	helpMenu.add(aboutAction);
    	
    	menuBar.add(hyperbolaMenu);
    	menuBar.add(helpMenu);
    }

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(coolBar.getStyle());
		toolbar.add(addContactAction);
		toolbar.add(chatAction);
		coolBar.add(toolbar);
	}
	
	protected void fillTrayItem(IMenuManager trayItem) {
		trayItem.add(aboutAction);
		trayItem.add(exitAction);
	}
}
