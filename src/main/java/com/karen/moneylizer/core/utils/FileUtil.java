package com.karen.moneylizer.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class FileUtil {

	private final static String PROPERTIES_FILE_NAME = "application.properties";
	/**
	 * Read a short text file all at once
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath) {
		String value = "";
		URL url = Resources.getResource(filePath);
		try {
			value = Resources.toString(url, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * Read property text file and return the value for the specific property
	 * 
	 * @param filePath
	 * @param propertyName
	 * @return
	 */
	public static String readPropertyFile(String filePath, String propertyName) {
		String value = "";
		Properties prop = new Properties();
		try (InputStream inputStream = ClassLoader.getSystemClassLoader()
				.getResourceAsStream(PROPERTIES_FILE_NAME)) {
			if (inputStream != null) {
				prop.load(inputStream);
				value = prop.getProperty(propertyName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
