package com.noname.guess.number.core;

import java.io.Serializable;

public interface Player extends Serializable, Comparable<Player> {
	String getName();
	int getRating();
	void setRating(int rating);
}
