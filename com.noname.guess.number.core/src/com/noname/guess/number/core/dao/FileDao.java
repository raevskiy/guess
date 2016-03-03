package com.noname.guess.number.core.dao;

import java.io.File;
import java.io.IOException;

public interface FileDao<T> {
	T deserialize(File file) throws IOException, ClassNotFoundException;
	void serialize(T obj, File file) throws IOException;
}
