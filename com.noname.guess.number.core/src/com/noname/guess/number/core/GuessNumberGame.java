package com.noname.guess.number.core;

/**
 * Guess a Number game
 * @author Северин
 *
 */
public interface GuessNumberGame {
	/**
	 * Starts a game on the specified difficulty level
	 * The game, that is already started is restarted (like calling cancel + start)
	 * @param level - level of difficulty
	 */
	void start(GuessNumberLevel level);
	/**
	 * Cancels a started game. The game, that is already over, cannot be stopped
	 * @return
	 */
	int cancel();
	/**
	 * Make a guess. Can end the game, if a guess is correct.
	 * @param value - guessed value
	 * @return 0 if generated value == value,
	 *  a negative number if generated value < value,
	 *  a positive number if generated value > value.
	 *  No other assumptions are true. 
	 */
	int guess(int value);
	/**
	 * Is game in progress?
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
