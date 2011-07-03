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
 * Addison-Wesley, Summer 2005
 *
 * Contributors:
 *     Jean-Michel Lemieux and Jeff McAffer - initial implementation
 *     Nick Edgar and Pascal Rapicault - additional work for EclipseCon 2005 tutorial
 *         http://www.eclipsecon.org/presentations/EclipseCon2005_Tutorial26.pdf
 *     Pavel Samolisov - building and additional features
 *******************************************************************************/
package org.rcpbook.hyperbola.ui.preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.rcpbook.hyperbola.ui.Application;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public static final String AUTO_LOGIN = "prefs_auto_login";
	
	private ScopedPreferenceStore preferences;	
	
	public GeneralPreferencePage() {
		super(GRID);
		preferences = new ScopedPreferenceStore(new ConfigurationScope(), Application.PLUGIN_ID);
		setPreferenceStore(preferences);
	}
	
	public void init(IWorkbench workbench) {		
	}

	@Override
	protected void createFieldEditors() {
		BooleanFieldEditor boolEditor = new BooleanFieldEditor(AUTO_LOGIN, 
				"Login automaticaly at startup", 
				getFieldEditorParent());
		addField(boolEditor);
	}

	@Override
	public boolean performOk() {
		try {
			preferences.save();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return super.performOk();
	}
}
