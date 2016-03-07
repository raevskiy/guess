package com.noname.guess.number.core;

/**
 * Level implementation
 * @author Северин
 *
 */
public class GuessNumberLevelImpl implements GuessNumberLevel {
	private final int lowerBound;
	private final int upperBound;
	private final String name;

	/**
	 * Level constructor
	 * @param name - level name, cannot be null
	 * @param lowerBound - lower bound for generated level, <= upper bound
	 * @param upperBound - upper bound for generated level, >= lower bound
	 */
	public GuessNumberLevelImpl(
			String name,
			int lowerBound,
			int upperBound) {
		if (name == null)
			throw new IllegalArgumentException("Upper cannot be null");
		if (upperBound < lowerBound)
			throw new IllegalArgumentException("Upper bound cannot be less then lower bound");
		if (upperBound < 0 || lowerBound < 0)
			throw new IllegalArgumentException("Upper and lower bound cannot be negative");

		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getLowerBound() {
		return lowerBound;
	}

	@Override
	public int getUpperBound() {
		return upperBound;
	}
}
