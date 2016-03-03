package com.noname.guess.number.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GuessNumberGameImpl implements GuessNumberGame {
	private static final double LOG_2 = Math.log(2);
	private static final int NORMAL_PENALTY = 1;
	private int number;
	private int attempts;
	private boolean isInProgress;
	private List<GameEventListener> listeners = new ArrayList<>();
	
	private int rating;
	private int ratingMax;
	private int binarySearchAttempts;
	private int bruteforcePenalty;
	
	@Override
	public void start(GuessNumberLevel level) {
		isInProgress = true;
		int lowerBound = level.getLowerBound(); 
		int upperBound = level.getUpperBound();
		number = ThreadLocalRandom.current().nextInt(
				lowerBound,
				upperBound + 1);
		ratingMax = upperBound - lowerBound;
		rating = ratingMax + NORMAL_PENALTY;
		attempts = 0;
		binarySearchAttempts = (int)Math.ceil(Math.log(ratingMax) / LOG_2);
		int bruteforceAttempts = ratingMax - binarySearchAttempts;
		int bruteforcePenalty = bruteforceAttempts / 10;  
		this.bruteforcePenalty = (bruteforcePenalty >= NORMAL_PENALTY) ? bruteforcePenalty : NORMAL_PENALTY;
		
		for (GameEventListener listener : listeners)
			listener.onGameStrated();
	}

	@Override
	public void cancel() {
		validateProgress();
		
		rating = 0;
		stop();
	}
	
	private void stop() {
		isInProgress = false;
		for (GameEventListener listener : listeners)
			listener.onGameStopped();
	}
	
	private void validateProgress() {
		if (!isInProgress)
			throw new IllegalStateException("Game is not in progress");
	}

	@Override
	public int guess(int value) {
		validateProgress();
		
		attempts++;
		int penalty = (attempts <= binarySearchAttempts) ? NORMAL_PENALTY : bruteforcePenalty;
		rating -= penalty;
		rating = Math.max(rating, 1);

		int outcome = value - number;
		if (outcome == 0)
			stop();
		return outcome;
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
