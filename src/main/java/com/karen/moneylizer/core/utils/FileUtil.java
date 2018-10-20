package com.karen.moneylizer.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class FileUtil {

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
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			String propFileName = "application.properties";
			inputStream = ClassLoader.getSystemClassLoader()
					.getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
				value = prop.getProperty(propertyName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

}
