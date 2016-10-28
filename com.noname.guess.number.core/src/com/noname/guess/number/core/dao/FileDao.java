package com.noname.guess.number.core.dao;

import java.io.File;
import java.io.IOException;

/**
 * Dao for loading and saving in files
 *
 * @param <T>
 */
public interface FileDao<T> {
	/**
	 * Deserialize an object from a file
	 * @param file - the file that keeps the object 
	 * @return - object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	T deserialize(File file) throws IOException, ClassNotFoundException;
	/**
	 * Serialize an object to a file
	 * @param obj - serialized object
	 * @param file - the file for object storage
	 * @throws IOException
	 */
	void serialize(T obj, File file) throws IOException;
}
