package com.noname.guess.number.core;

public class GuessNumberLevelImpl implements GuessNumberLevel {
	private final int lowerBound;
	private final int upperBound;
	private final String name;

	public GuessNumberLevelImpl(
			String name,
			int lowerBound,
			int upperBound) {
		if (name == null)
			throw new IllegalArgumentException("Upper cannot be null");
		if (upperBound < lowerBound)
			throw new IllegalArgumentException("Upper bound cannot be less then lower bound");
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
