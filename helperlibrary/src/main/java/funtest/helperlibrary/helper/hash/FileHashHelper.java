package funtest.helperlibrary.helper.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Helper class that allows hashing functions to be performed
 * 
 * @author codersparks
 * 
 */
public class FileHashHelper {

	/**
	 * Generate a MD5 based hash of a specified file
	 * 
	 * @param file
	 *            The file to perform the hashing of
	 * @return The MD5 hash of the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String MD5Hash(File file) throws FileNotFoundException,
			IOException {
		return DigestUtils.md5Hex(new FileInputStream(file));
	}
}
