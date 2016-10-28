package com.noname.guess.number.core.dao;
 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileDaoImpl<T> implements FileDao<T> {
	@Override
    public T deserialize(File file) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        @SuppressWarnings("unchecked")
		T obj = (T) ois.readObject();
        ois.close();
        return obj;
    }
 
	@Override
    public void serialize(T obj, File file)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }
}