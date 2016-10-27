package com.noname.guess.number.core;

public class Player implements Comparable<Player> {
	private String name;
	private int rating = 0;
	
	public Player(String name, int rating) {
		this.name = name;
		this.rating = rating;
	}

	public String getName() {
		return name;
	}

	public int getRating() {
		return rating;
	}

	@Override
	public int compareTo(Player o) {
		int ratingDiff = o.getRating() - this.rating;
		if (ratingDiff != 0)
			return ratingDiff;
		else
			return this.name.compareTo(o.getName());
	}
}
