package funtest.helperlibrary.helper.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import funtest.helperlibrary.util.process.ProcessResult;

/**
 * Class that provides SSH functions
 * @author codersparks
 *
 */
public class SSHHelper {
	
	/**
	 * Function to execute a command on a remote server
	 * @param host The host to run the command on
	 * @param user The user to run the command as
	 * @param password The password of the user
	 * @param command The command to be run
	 * @param timeout How long should be waited for the command to return
	 * @return {@link ProcessResult} of the command
	 * @throws IOException
	 */
	public static ProcessResult executeCommand (String host, String user, String password, String command, long timeout) throws IOException {
		
		Connection conn = new Connection(host);
		
		conn.connect();
		
		boolean isAuthenticated = conn.authenticateWithPassword(user, password);
		
		if (isAuthenticated == false) {
			throw new IOException("Athentication failed.");
		}
		
		Session sess = conn.openSession();
		
		sess.execCommand(command);
		sess.waitForCondition(ChannelCondition.EOF, timeout);
		
		ProcessResult processResult = new ProcessResult();
		
		InputStream stdout = new StreamGobbler(sess.getStdout());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		
		StringBuilder stdoutBuilder = new StringBuilder();
		
		while(true) {
			String line = br.readLine();
			if(line == null) {
				break;
			}
			stdoutBuilder.append(line).append("\n");
		}
		
		br.close();

		InputStream stderr = new StreamGobbler(sess.getStderr());
		
		br = new BufferedReader(new InputStreamReader(stderr));
		
		StringBuilder stderrBuilder = new StringBuilder();
		
		while(true) {
			String line = br.readLine();
			if(line == null) {
				break;
			}
			stderrBuilder.append(line).append("\n");
		}
		
		processResult.setStderr(stderrBuilder.toString());
		processResult.setStdout(stdoutBuilder.toString());
		
		Integer exitCode = sess.getExitStatus();
		
		//System.out.println("ExitCode: " + exitCode);
		
		processResult.setExitCode(exitCode);
		
		br.close();
		
		sess.close();
		
		conn.close();
		
		return processResult;
		
	}
}
