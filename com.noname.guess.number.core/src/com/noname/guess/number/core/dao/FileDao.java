package com.noname.guess.number.core.dao;

import java.io.IOException;

public interface FileDao<T> {
	T deserialize(String fileName) throws IOException, ClassNotFoundException;
	void serialize(T obj, String fileName) throws IOException;
}
