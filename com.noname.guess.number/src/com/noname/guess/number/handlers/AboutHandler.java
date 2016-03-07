/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package com.noname.guess.number.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.noname.guess.number.i18n.Messages;

/**
 * Hanldes "About" dialog showing
 * @author Северин
 *
 */
public class AboutHandler {
	@Execute
	public void execute(Shell shell) {
		MessageDialog.openInformation(
				shell,
				Messages.AboutHandler_AboutTitle,
				Messages.AboutHandler_AboutDesc);
	}
}
