package org.jaredstevens.servers.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;

public class Utilities {
	private static Logger logger = LoggerFactory.getLogger(Utilities.class);
	/**
	 * Takes two strings as input and builds a valid path out of them by placing a slash inbetween them.
	 * This method assumes a unix-type file system and uses forward slashes.
	 * @param part1 The first part of the path
	 * @param part2 The second part of the path
	 * @return The combination of part1 and part2.
	 * Example 1:
	 * If given input:
	 *   part1 = "/home/user"
	 *   part2 = "subdir1/subdir2/file.txt"
	 * output would be: "/home/user/file/subdir1/subdir2/file.txt"
	 * Notice the addition of a slash between the two strings.
	 * Example 2:
	 * If given input:
	 *   part1 = "/home/user/"
	 *   part2 = "/subdir1/subdir2/file.txt"
	 * output would be: "/home/user/subdir1/subdir2/file.txt"
	 * Notice the removal of a slash between the two strings.
	 */
	public static String buildPath(String part1, String part2) {
		String retVal;
		if(part1.endsWith("/") && part2.startsWith("/")) {
			part1 = part1.substring(0, part1.length()-1);
			retVal = part1+part2;
		} else if(!part1.endsWith("/") && !part2.startsWith("/")) {
			retVal = part1+"/"+part2;
		} else retVal = part1+part2;
		return retVal;
	}

	/**
	 * Writes a file to the local file system
	 * @param fileData The data to go into the file
	 * @param path The path where the file is located
	 * @param filename The file name to write to
	 * @throws java.io.IOException
	 */
	public static void writeFile(byte[] fileData, String path, String filename) throws IOException {
		File dir = new File(path);
		if(!dir.exists()) throw new FileNotFoundException("Directory "+path+" doesn't exist.");

		// Create the file on server
		File serverFile = new File(dir.getAbsolutePath()
				+ File.separator + filename);
		if(serverFile.exists()) Utilities.logger.warn("File already exists. Overwriting :"+serverFile.getAbsoluteFile());
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(serverFile));
		stream.write(fileData);
		stream.close();
	}

	public static boolean deleteFile(String path, String filename) throws IOException {
		File dir = new File(path);
		if(!dir.exists()) throw new FileNotFoundException("Directory "+path+" doesn't exist.");
		File serverFile = new File(dir.getAbsolutePath()
				+ File.separator + filename);
		boolean result = serverFile.delete();
		return result;
	}
}
