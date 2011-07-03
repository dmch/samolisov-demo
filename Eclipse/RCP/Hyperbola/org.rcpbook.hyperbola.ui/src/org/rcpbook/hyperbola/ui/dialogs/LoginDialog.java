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
package org.rcpbook.hyperbola.ui.dialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.branding.IProductConstants;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.rcpbook.hyperbola.ui.Application;
import org.rcpbook.hyperbola.ui.model.ConnectionDetails;
import org.rcpbook.hyperbola.ui.preferences.GeneralPreferencePage;

/**
 * Login dialog, which prompts for the user's account info, and has Login and
 * Cancel buttons.
 */
public class LoginDialog extends Dialog {

	private static final String PASSWORD = "password";
	
	private static final String SERVER = "server";
	
	private static final String SAVED = "saved-connections";
	
	private static final String LAST_USER = "last-connection";
	
	private Combo userIdText;

	private Text serverText;

	private Text passwordText;
	
	private Image[] images;

	private ConnectionDetails connectionDetails;
	
	private Map<String, ConnectionDetails> savedDetails = new HashMap<String, ConnectionDetails>();

	public LoginDialog(Shell parentShell) {
		super(parentShell);
		loadDescriptors();
	}

	public void saveDescriptors() {
		try {
			ISecurePreferences preferences = SecurePreferencesFactory.getDefault();		
			preferences.put(LAST_USER, connectionDetails.getUserId(), false);
			ISecurePreferences connections = preferences.node(SAVED);
			for (Entry<String, ConnectionDetails> entry : savedDetails.entrySet()) {
				ConnectionDetails details = entry.getValue();
				ISecurePreferences connection = connections.node(entry.getKey());
				connection.put(SERVER, details.getServer(), false);
				connection.put(PASSWORD, details.getPassword(), true);
			}		
	
			connections.flush();		
		}
		catch (StorageException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadDescriptors() {
		try {
			ISecurePreferences preferences = SecurePreferencesFactory.getDefault();			
			ISecurePreferences connections = preferences.node(SAVED);
			String[] userNames = connections.childrenNames();
			for (int i = 0; i < userNames.length; i++) {
				String userName = userNames[i];
				ISecurePreferences node = connections.node(userName);
				savedDetails.put(userName, new ConnectionDetails(
						userName,
						node.get(SERVER, ""),
						node.get(PASSWORD, "")));
			}
			connectionDetails = savedDetails.get(preferences.get(LAST_USER, ""));
		}
		catch (StorageException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		String userId = userIdText.getText();
		String server = serverText.getText();
		String password = passwordText.getText();
		
		connectionDetails = new ConnectionDetails(userId, server, password);
		savedDetails.put(userId, connectionDetails);
		
		if (buttonId == IDialogConstants.OK_ID || buttonId == IDialogConstants.CANCEL_ID)
			saveDescriptors();
		
		super.buttonPressed(buttonId);
	}	
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Hyperbola Login");
		IProduct product = Platform.getProduct();
		if (product != null) {
			String bundleId = product.getDefiningBundle().getSymbolicName();
			String imagesUrls[] = parseCSL(product.getProperty(IProductConstants.WINDOW_IMAGES));
			if (imagesUrls.length > 0) {
				images = new Image[imagesUrls.length];
				for (int i = 0; i < imagesUrls.length; i++) {
					ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(bundleId, imagesUrls[i]);
					images[i] = descriptor.createImage(true);
				}
				newShell.setImages(images);
			}			
		}
	}

    public static String[] parseCSL(String csl) {
        if (csl == null)
            return null;

        StringTokenizer tokens = new StringTokenizer(csl, ","); //$NON-NLS-1$
        List<String> array = new ArrayList<String>(10);
        while (tokens.hasMoreTokens())
            array.add(tokens.nextToken().trim());

        return (String[]) array.toArray(new String[array.size()]);
    }
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label accountLabel = new Label(composite, SWT.NONE);
		accountLabel.setText("Account details:");
		accountLabel.setLayoutData(new GridData(GridData.BEGINNING,
				GridData.CENTER, false, false, 2, 1));

		Label userIdLabel = new Label(composite, SWT.NONE);
		userIdLabel.setText("&User ID:");
		userIdLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER,
				false, false));

		userIdText = new Combo(composite, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gridData.widthHint = convertHeightInCharsToPixels(20);
		userIdText.setLayoutData(gridData);
		userIdText.addListener(SWT.Modify, new Listener() {			
			public void handleEvent(Event event) {
				ConnectionDetails details = savedDetails.get(userIdText.getText());
				if (details != null) {
					serverText.setText(details.getServer());
					passwordText.setText(details.getPassword());
				}
			}
		});

		Label serverLabel = new Label(composite, SWT.NONE);
		serverLabel.setText("&Server:");
		serverLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER,
				false, false));

		serverText = new Text(composite, SWT.BORDER);
		serverText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, false));

		Label passwordLabel = new Label(composite, SWT.NONE);
		passwordLabel.setText("&Password:");
		passwordLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER,
				false, false));

		passwordText = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, false));
		
		final Button autoLogin = new Button(composite, SWT.CHECK);
		autoLogin.setText("Login &automaticaly at startup");
		autoLogin.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true, 2, 1));
		autoLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					IEclipsePreferences preferences = new ConfigurationScope().getNode(Application.PLUGIN_ID);
					preferences.putBoolean(GeneralPreferencePage.AUTO_LOGIN, autoLogin.getSelection());
					preferences.flush();			
				} catch (BackingStoreException ex) {					
					ex.printStackTrace();
				}
			}			
		});
		
		Preferences preferences = new ConfigurationScope().getNode(Application.PLUGIN_ID);
		autoLogin.setSelection(preferences.getBoolean(GeneralPreferencePage.AUTO_LOGIN, false));		
		
        String lastUser = "none";
        if (connectionDetails != null)
            lastUser = connectionDetails.getUserId();
        initializeUsers(lastUser);
		
		return composite;
	}

	protected void initializeUsers(String defaultUser) {
		userIdText.removeAll();
		passwordText.setText("");
		serverText.setText("");
		for (String user : savedDetails.keySet())
			userIdText.add(user);
		int index = Math.max(userIdText.indexOf(defaultUser), 0);
		userIdText.select(index);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createDeleteUserButton(parent);
		createButton(parent, IDialogConstants.OK_ID, "&Login", true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);		
	}
	
	private Button createDeleteUserButton(Composite parent) {
		Button deleteUserButton = createButton(parent, IDialogConstants.CLIENT_ID, "&Delete User", false);
		deleteUserButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				savedDetails.remove(userIdText.getText());
				initializeUsers("");
			}			
		});
		
		return deleteUserButton;
	}

	@Override
	protected void okPressed() {
		String userId = userIdText.getText();
		String server = serverText.getText();
		String password = passwordText.getText();
		if (userId.equals("")) {
			MessageDialog.openError(getShell(), "Invalid User ID",
					"User ID field must not be blank.");
			return;
		}
		if (server.equals("")) {
			MessageDialog.openError(getShell(), "Invalid Server",
					"Server field must not be blank.");
			return;
		}
		connectionDetails = new ConnectionDetails(userId, server, password);
		super.okPressed();
	}

	/**
	 * Returns the connection details entered by the user, or <code>null</code>
	 * if the dialog was canceled.
	 */
	public ConnectionDetails getConnectionDetails() {
		return connectionDetails;
	}

	@Override
	public boolean close() {
		if (images != null) {
			for (int i = 0; i < images.length; i++)
				images[i].dispose();
		}
		
		return super.close();
	}	
}
