package com.noname.guess.number.core;

public class PlayerImpl implements Player {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6438257340950101497L;
	private String name;
	private int rating = 0;
	
	PlayerImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getRating() {
		return rating;
	}

	@Override
	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public int compareTo(Player o) {
		int ratingDiff = this.rating - o.getRating();
		if (ratingDiff != 0)
			return ratingDiff;
		else
			return this.name.compareTo(o.getName());
	}
}
