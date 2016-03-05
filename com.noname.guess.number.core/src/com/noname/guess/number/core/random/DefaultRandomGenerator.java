package com.noname.guess.number.core.random;

import java.util.concurrent.ThreadLocalRandom;

public class DefaultRandomGenerator implements RandomGenerator {

	@Override
	public int generateRandomInt(int lowerBound, int upperBound) {
		return ThreadLocalRandom.current().nextInt(
				lowerBound,
				upperBound + 1);
	}

}
