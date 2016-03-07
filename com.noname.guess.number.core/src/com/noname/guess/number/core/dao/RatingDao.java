package com.noname.guess.number.core.dao;

import java.io.IOException;
import java.util.Map;

/**
 * Dao for loading and saving rating
 * @author �������
 *
 */
public interface RatingDao {
	/**
	 * Deserialize a rating map (player names and their scores)
	 * @return rating map
	 */
	Map<String, Integer> deserialize();
	/**
	 * Serialize a rating map (player names and their scores)
	 * @param rating - rating map to serialize
	 * @throws IOException
	 */
	void serialize(Map<String, Integer> rating) throws IOException;

}
