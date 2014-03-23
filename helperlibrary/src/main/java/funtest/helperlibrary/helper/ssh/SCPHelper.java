package funtest.helperlibrary.helper.ssh;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

/**
 * Class to perform SCP functions
 * @author codersparks
 *
 */
public class SCPHelper {
	/**
	 * Function to copy a remote file to a local destination
	 * @param host The host where the file resides
	 * @param user The user name to use
	 * @param password The pass word to use
	 * @param remoteFile The file to be copied from the remote server
	 * @param destination Where the file should be copied to
	 * @throws IOException
	 */
	public static void getFile(String host, String user, String password,
			String remoteFile, String destination) throws IOException {

		Connection conn = new Connection(host);
		
		conn.connect();
		
		boolean isAuthenticated = conn.authenticateWithPassword(user, password);
		
		if (isAuthenticated == false) {
			throw new IOException("Athentication failed.");
		}
		
		SCPClient scpClient = new SCPClient(conn);
		
		scpClient.get(remoteFile, destination);
		
		conn.close();
	}
}
