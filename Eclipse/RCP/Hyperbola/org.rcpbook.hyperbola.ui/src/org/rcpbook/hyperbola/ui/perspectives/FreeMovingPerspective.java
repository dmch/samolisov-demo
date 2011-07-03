package org.rcpbook.hyperbola.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.rcpbook.hyperbola.ui.views.ContactsView;

public class FreeMovingPerspective implements IPerspectiveFactory {

	public static final String ID = "org.rcpbook.hyperbola.ui.freemoving";
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.addView(ContactsView.ID, IPageLayout.LEFT, 0.4f, layout.getEditorArea());
		layout.getViewLayout(ContactsView.ID).setCloseable(false);
	}
}
