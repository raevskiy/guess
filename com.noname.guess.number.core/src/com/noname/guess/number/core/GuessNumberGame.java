package com.noname.guess.number.core;

public interface GuessNumberGame {
	/**
	 * Starts a game on the specified difficulty level.
	 * The game, that is already started is restarted (like calling cancel + start)
	 * @param level - level of difficulty
	 */
	void start(GuessNumberLevel level);
	/**
	 * Cancels a started game. If the game is already over, nothing happens.
	 * @return the guessed number
	 */
	int cancel();
	/**
	 * Make a guess. Can end the game, if a guess is correct.
	 * @param value - guessed value
	 * @return 0 if generated value == value,
	 *  a negative number if generated value < value,
	 *  a positive number if generated value > value.
	 * The absolute value of a returned value is non-deterministic. 
	 */
	int guess(int value);
	/**
	 * Is the game in progress?
	 * @return
	 */
	boolean isInProgress();
	
	/**
	 * Adds an event listener for this game. 
	 * @param listener
	 */
	void addGameEventListener(GameEventListener listener);
	/**
	 * Removes the specified event listener for this game.
	 * @param listener
	 */
	void removeGameEventListener(GameEventListener listener);
	
	/**
	 * Gets the number of attempts used in this game (no matter is it in progress or not)
	 * @return
	 */
	int getAttempts();
	/**
	 * Gets the rating achieved in this game (no matter is it in progress or not)
	 * @return
	 */
	int getRating();
}
