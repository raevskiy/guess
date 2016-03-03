package com.noname.guess.number.core.dao;

import java.io.IOException;
import java.util.Map;

public interface RatingDao {
	Map<String, Integer> deserialize();
	void serialize(Map<String, Integer> rating) throws IOException;

}
