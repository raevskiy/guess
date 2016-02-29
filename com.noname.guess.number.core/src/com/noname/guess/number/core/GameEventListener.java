package com.noname.guess.number.core;

import java.util.EventListener;

public interface GameEventListener extends EventListener {
	void onGameStrated();
	void onGameStopped();
}
