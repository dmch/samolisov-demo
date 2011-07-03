package org.rcpbook.hyperbola.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.rcpbook.hyperbola.ui.Application;
import org.rcpbook.hyperbola.ui.model.Session;

public class DebugConsole extends MessageConsole {

	private MessageConsoleStream outMessageStream;
	
	private MessageConsoleStream inMessageStream;
	
	private PacketListener outListener = new PacketListener() {		
		public void processPacket(Packet packet) {
			outMessageStream.println(packet.toXML());
		}
	};
	
	private PacketListener inListener = new PacketListener() {		
		public void processPacket(Packet packet) {
			inMessageStream.println(packet.toXML());
		}
	};
	
	public DebugConsole() {
		super("XMPP Debug", AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, 
				"icons/debug_persp.gif"));
		outMessageStream = newMessageStream();
		outMessageStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		
		inMessageStream = newMessageStream();
		inMessageStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		Session.getInstance().getConnection().addPacketWriterListener(outListener, null);
		Session.getInstance().getConnection().addPacketListener(inListener, null);
	}

	@Override
	protected void dispose() {
		Session.getInstance().getConnection().removePacketWriterListener(outListener);
		Session.getInstance().getConnection().removePacketListener(inListener);
	}	
}
