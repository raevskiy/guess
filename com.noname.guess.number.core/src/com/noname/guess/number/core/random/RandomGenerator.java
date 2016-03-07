package com.noname.guess.number.core.random;

/**
 * Random Integer Generator
 * @author Северин
 *
 */
public interface RandomGenerator {
	/**
	 * Generate random integer
	 * @param lowerBound lower bound for generated value, inclusive
	 * @param upperBound upper bound for generated value, inclusive
	 * @return
	 */
	int generateRandomInt(int lowerBound, int upperBound);
}
