package com.noname.guess.number.core;

public class GuessNumberLevelImpl implements GuessNumberLevel {
	private final int lowerBound;
	private final int upperBound;
	private final String name;
	private final boolean isDefault;

	public GuessNumberLevelImpl(
			String name,
			int lowerBound,
			int upperBound) {
		this(name, lowerBound, upperBound, false);
	}
	
	public GuessNumberLevelImpl(
			String name,
			int lowerBound,
			int upperBound,
			boolean isDefault) {
		if (upperBound < lowerBound)
			throw new IllegalArgumentException("Upper bound cannot be less then lower bound");
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.name = name;
		this.isDefault = isDefault;
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
	
	@Override
	public boolean isDefault() {
		return isDefault;
	}
}
