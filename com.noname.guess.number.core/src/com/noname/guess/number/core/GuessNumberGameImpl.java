package com.noname.guess.number.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GuessNumberGameImpl implements GuessNumberGame {
	private int number;
	private int attempts;
	private boolean isInProgress;
	private List<GameEventListener> listeners = new ArrayList<>();

	@Override
	public void start(GuessNumberLevel level) {
		isInProgress = true;
		attempts = 0;
		number = ThreadLocalRandom.current().nextInt(
				level.getLowerBound(),
				level.getUpperBound() + 1);
		
		for (GameEventListener listener : listeners)
			listener.onGameStrated();
	}

	@Override
	public void stop() {
		isInProgress = false;
		for (GameEventListener listener : listeners)
			listener.onGameStopped();
	}

	@Override
	public int guess(int value) {
		if (!isInProgress)
			throw new IllegalStateException("Game is not in progress");
		
		attempts++;
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

}
