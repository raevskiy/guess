package com.noname.guess.number.core;

import java.util.SortedSet;

public interface GuessNumberGame {
	void start(GuessNumberLevel level);
	void cancel();
	int guess(int value);
	
	void addGameEventListener(GameEventListener listener);
	void removeGameEventListener(GameEventListener listener);
	
	int getAttempts();
	int getRating();
	SortedSet<Player> getSortedPlayers();
}
