package com.noname.guess.number.i18n;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.noname.guess.number.i18n.messages"; //$NON-NLS-1$
	public static String AboutHandler_AboutDesc;
	public static String AboutHandler_AboutTitle;
	public static String GamePart_DefaultPlayerName;
	public static String GamePart_Difficulty;
	public static String GamePart_GameCancelled;
	public static String GamePart_Greater;
	public static String GamePart_InputPlayerName;
	public static String GamePart_Less;
	public static String GamePart_PlayerNameTooLong;
	public static String GamePart_PlayerNameTooShort;
	public static String GamePart_RangeHint;
	public static String GamePart_RightAnswer;
	public static String GamePart_SerializeErrorMessage;
	public static String GamePart_SerializeErrorTitle;
	public static String GamePart_StartGame;
	public static String GamePart_Surrender;
	public static String GamePart_TryNumber;
	public static String GamePart_VictoryTitle;
	public static String GuessNumberAddon_LevelEasy;
	public static String GuessNumberAddon_LevelHard;
	public static String GuessNumberAddon_LevelMedium;
	public static String QuitHandler_ConfirmationMessage;
	public static String QuitHandler_ConfirmationTitle;
	public static String RatingPart_PlayerName;
	public static String RatingPart_PlayerRating;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
