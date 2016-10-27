package com.noname.guess.number.core;

import java.util.ArrayList;
import java.util.List;

import com.noname.guess.number.core.random.DefaultRandomGenerator;
import com.noname.guess.number.core.random.RandomGenerator;

public class GuessNumberGameImpl implements GuessNumberGame {
	private static final double LOG_2 = Math.log(2);
	private static final int NORMAL_PENALTY = 1;
	
	private int number;
	private int attempts;
	private boolean isInProgress;
	private List<GameEventListener> listeners = new ArrayList<>();
	private RandomGenerator randomGenerator;
	
	private int rating;
	private int ratingMax;
	private int binarySearchAttempts;
	private int bruteforcePenalty;
	
	public GuessNumberGameImpl() {
		this(new DefaultRandomGenerator());
	}
	
	public GuessNumberGameImpl(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}
	
	@Override
	public void start(GuessNumberLevel level) {
		if (isInProgress)
			cancel();

		isInProgress = true;
		int lowerBound = level.getLowerBound(); 
		int upperBound = level.getUpperBound();
		number = randomGenerator.generateRandomInt(lowerBound, upperBound);
		ratingMax = upperBound - lowerBound;
		rating = ratingMax;
		attempts = 0;
		binarySearchAttempts = (int)Math.ceil(Math.log(ratingMax) / LOG_2);
		int bruteforceAttempts = ratingMax - binarySearchAttempts;
		int bruteforcePenalty = bruteforceAttempts / 10;  
		this.bruteforcePenalty = (bruteforcePenalty >= NORMAL_PENALTY) ? bruteforcePenalty : NORMAL_PENALTY;
		
		for (GameEventListener listener : listeners)
			listener.onGameStarted();
	}

	@Override
	public int cancel() {
		rating = 0;
		stop();
		return number;
	}
	
	@Override
	public boolean isInProgress() {
		return isInProgress;
	}
	
	private void stop() {
		if (isInProgress) {
			isInProgress = false;
			for (GameEventListener listener : listeners)
				listener.onGameStopped();
		}
	}
	
	@Override
	public int guess(int value) {
		if (!isInProgress)
			throw new IllegalStateException("The game is not in progress");
		
		attempts++;
		updateRating();
		
		//hide the difference to prevent abuse
		int outcome = Integer.signum(value - number);
		if (outcome == 0) {
			stop();
		}
		return outcome;
	}
	
	private void updateRating() {
		//The first attempt has no penalty
		if (attempts > 1) {
			int penalty = (attempts <= binarySearchAttempts) ? NORMAL_PENALTY : bruteforcePenalty;
			rating -= penalty;
			rating = Math.max(rating, 1);
		};
	}

	@Override
	public void addGameEventListener(GameEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeGameEventListener(GameEventListener listener) {
		listeners.remove(listener);
	}

	@Override
	public int getAttempts() {
		return attempts;
	}
	
	@Override
	public int getRating() {
		return rating;
	}
}
