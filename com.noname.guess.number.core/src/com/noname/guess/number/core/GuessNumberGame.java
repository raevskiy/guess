package com.noname.guess.number.core;

public interface GuessNumberGame {
	void start(GuessNumberLevel level);
	void cancel();
	int guess(int value);
	
	void addGameEventListener(GameEventListener listener);
	void removeGameEventListener(GameEventListener listener);
	
	int getAttempts();
	int getRating();
}
