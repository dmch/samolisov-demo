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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.jivesoftware.smack.XMPPException;
import org.osgi.service.prefs.Preferences;
import org.rcpbook.hyperbola.ui.dialogs.LoginDialog;
import org.rcpbook.hyperbola.ui.model.ConnectionDetails;
import org.rcpbook.hyperbola.ui.model.Session;
import org.rcpbook.hyperbola.ui.preferences.GeneralPreferencePage;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	public static String PLUGIN_ID = "org.rcpbook.hyperbola.ui"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */	
	public Object start(IApplicationContext context) throws Exception {			
		Display display = PlatformUI.createDisplay();
		context.applicationRunning();
		logInstalledBundlesGroups();
		try {
            final Session session = Session.getInstance();
            if (!login(session))
                return IApplication.EXIT_OK;
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
	
    private boolean login(final Session session) {
    	boolean firstTry = true;
    	LoginDialog dialog = new LoginDialog(null);
    	while (session.getConnection() == null || !session.getConnection().isAuthenticated()) {
    		ConnectionDetails details = dialog.getConnectionDetails();
    		if (!isAutoLogin() || details == null || !firstTry) {
    			if (dialog.open() != Window.OK)
    				return false;
    			details = dialog.getConnectionDetails();
    		}
    		firstTry = false;
    		session.setConnectionDetails(details);
    		connectWithProgress(session);
    	}

        return true;
    }
    
    private boolean isAutoLogin() {
    	Preferences preferences = new ConfigurationScope().getNode(Application.PLUGIN_ID);
    	return preferences.getBoolean(GeneralPreferencePage.AUTO_LOGIN, false);
    }
    
    private void connectWithProgress(final Session session) {
    	ProgressMonitorDialog progress = new ProgressMonitorDialog(null);
    	progress.setCancelable(true);
    	try {
    		progress.run(true, true, new IRunnableWithProgress() {				
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					try {
						session.connectAndLogin(monitor);						
					}
					catch (XMPPException e) {
						throw new InvocationTargetException(e);
					}
				}
			});
    	}
    	catch (InvocationTargetException e) {
			// TODO: handle exception
		}
    	catch (InterruptedException e) {
			// TODO: handle exception
		}
    }
    
    private void logInstalledBundlesGroups() {
    	IBundleGroupProvider[] providers = Platform.getBundleGroupProviders();
		for (IBundleGroupProvider provider : providers)
		{
			System.out.println(provider.getName() + " " + provider.getClass().getName());
			IBundleGroup[] groups = provider.getBundleGroups();
			for (IBundleGroup group : groups)
				System.out.println("--- " + group.getName());
		}
    }
}