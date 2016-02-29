package com.noname.guess.number.core;

public interface GuessNumberGame {
	void start(GuessNumberLevel level);
	int guess(int value);
	void stop();
	void addGameEventListener(GameEventListener listener);
	void removeGameEventListener(GameEventListener listener);
	int getAttempts();
}
