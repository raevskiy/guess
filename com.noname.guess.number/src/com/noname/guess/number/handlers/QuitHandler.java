package com.noname.guess.number.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.noname.guess.number.core.GuessNumberGame;
import com.noname.guess.number.i18n.Messages;

public class QuitHandler {
	@Execute
	public void execute(
			GuessNumberGame game,
			IWorkbench workbench,
			Shell shell){
		if (!game.isInProgress() 
				|| MessageDialog.openConfirm(
						shell,
						Messages.QuitHandler_ConfirmationTitle,
						Messages.QuitHandler_ConfirmationMessage)) {
			workbench.close();
		}
	}
}
