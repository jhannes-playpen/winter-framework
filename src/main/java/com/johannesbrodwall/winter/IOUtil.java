package com.johannesbrodwall.winter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class IOUtil {

	public static String toString(URL url) {
		try {
			URLConnection connection = url.openConnection();
			try(InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
				return toString(reader);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("404 not found " + url);
		} catch (IOException e) {
			throw new RuntimeException("Failed to get content from " + url, e);
		}
	}

	public static String toString(Reader reader) throws IOException {
		char[] buffer = new char[1024*1024];
	    StringBuilder result = new StringBuilder();
	    int numCharsRead;
	    while ((numCharsRead = reader.read(buffer)) != -1) {
	        result.append(buffer, 0, numCharsRead);
	    }
		return result.toString();
	}

}
