package com.noname.guess.number.core;

/**
 * Guess a number game difficulty level
 * @author Северин
 *
 */
public interface GuessNumberLevel {
	/**
	 * Level name, human-readable
	 * @return
	 */
	String getName();
	/**
	 * Lower bound for generated value
	 * @return
	 */
	int getLowerBound();
	/**
	 * Upper bound for generated value
	 * @return
	 */
	int getUpperBound();
}
