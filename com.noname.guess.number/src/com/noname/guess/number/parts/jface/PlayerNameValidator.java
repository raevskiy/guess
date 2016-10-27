package com.noname.guess.number.parts.jface;

import org.eclipse.jface.dialogs.IInputValidator;

import com.noname.guess.number.i18n.Messages;

public class PlayerNameValidator implements IInputValidator {
	private static final int NAME_LEN_MIN = 1;
	private static final int NAME_LEN_MAX = 20;
	
	@Override
	public String isValid(String newText) {
		int len = newText.length();
		if (len < NAME_LEN_MIN)
			return Messages.GamePart_PlayerNameTooShort;
		if (len > NAME_LEN_MAX)
			return Messages.GamePart_PlayerNameTooLong;
		return null;
	}
}
