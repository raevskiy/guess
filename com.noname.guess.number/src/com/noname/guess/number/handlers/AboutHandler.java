package com.noname.guess.number.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.noname.guess.number.i18n.Messages;

public class AboutHandler {
	@Execute
	public void execute(Shell shell) {
		MessageDialog.openInformation(
				shell,
				Messages.AboutHandler_AboutTitle,
				Messages.AboutHandler_AboutDesc);
	}
}
