package com.noname.guess.number.core;

import java.util.EventListener;

/**
 * Listener for main event types of a game
 * @author Северин
 *
 */
public interface GameEventListener extends EventListener {
	/**
	 * Called when game is started
	 */
	void onGameStarted();
	/**
	 * Called when game is ended
	 */
	void onGameStopped();
}
