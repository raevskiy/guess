package com.noname.guess.number.core;

public interface GuessNumberGame {
	void start(GuessNumberLevel level);
	int cancel();
	int guess(int value);
	boolean isInProgress();
	
	void addGameEventListener(GameEventListener listener);
	void removeGameEventListener(GameEventListener listener);
	
	int getAttempts();
	int getRating();
}
