package com.noname.guess.number.core.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RatingDaoImpl implements RatingDao {
	private FileDao<Map<String, Integer>> fileDao = new FileDaoImpl<>();
	private File file;
	
	public RatingDaoImpl(File file) {
		this.file = file;
	}

	@Override
	public Map<String, Integer> deserialize() {
		if (file.exists()) {
			try {
				return fileDao.deserialize(file);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		return new HashMap<String, Integer>();
	}

	@Override
	public void serialize(Map<String, Integer> rating) throws IOException {
		fileDao.serialize(rating, file);
	}

}
