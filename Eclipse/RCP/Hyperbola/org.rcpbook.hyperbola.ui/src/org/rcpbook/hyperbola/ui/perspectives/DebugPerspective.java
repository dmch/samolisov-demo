package org.rcpbook.hyperbola.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.rcpbook.hyperbola.ui.views.ContactsView;

public class DebugPerspective implements IPerspectiveFactory {

	public static final String ID = "org.rcpbook.hyperbola.ui.debug";
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.addStandaloneView(ContactsView.ID, false, IPageLayout.LEFT, 0.4f, layout.getEditorArea());
		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 0.70f, layout.getEditorArea());
	}
}
