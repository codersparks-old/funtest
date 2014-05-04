package funtest.testharness.teststep.ssh;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import funtest.helperlibrary.helper.ssh.SCPHelper;
import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.teststep.AbstractTestStep;

/**
 * Implementaion of the {@link AbstractTestStep} abstract class to copy a file from a
 * remote server
 * 
 * @author codersparks
 * 
 */
public class SCPTestStep extends AbstractTestStep {

	public SCPTestStep() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestResult runTestStep() {
		this.testResult.logMessage("Running test step: " + alias + " Type: "
				+ this.getClass().getName());
		// for(String key : this.properties.stringPropertyNames()) {
		// System.out.println("\t=>Property: " + key + " Value: " +
		// this.properties.getProperty(key));
		// }

		try {
			String host = this.getProperty("host");
			this.testResult.logMessage("Host: " + host);
			String user = this.getProperty("user");
			this.testResult.logMessage("User: " + user);
			String password = this.getProperty("password");
			this.testResult.logMessage("Password: " + password);
			String remoteDirectory = this.getProperty("remotedirectory");
			if (!remoteDirectory.endsWith("/")) {
				remoteDirectory += "/";
			}
			this.testResult.logMessage("Remote directory: " + remoteDirectory);
			String remoteFile = this.getProperty("remotefile");
			this.testResult.logMessage("Remote File: " + remoteFile);
			String destination = this.getProperty("destination");
			if (destination == null || destination.length() < 1) {
				String workingDir = context
						.getEnvironmentProperty("workingDir");
				String testCaseName = context.getTestCaseName();

				destination = workingDir + "/" + testCaseName;

			}
			this.testResult.logMessage("Destination: " + destination);

			File destinationFile = new File(destination);

			if (!destinationFile.isDirectory()) {
				destinationFile.mkdirs();
			}
			SCPHelper.getFile(host, user, password, remoteDirectory
					+ remoteFile, destination);

			File destFile = new File(destination, remoteFile);

			if (destFile.canRead()) {
				this.testResult.logPass("File copied succesfully");
			} else {
				if (destFile.exists()) {
					this.testResult
							.logFailure("File copied successfully but cannot be read");
				} else {
					this.testResult.logFailure("File not copied");
				}
			}
		} catch (IOException e) {

			Throwable cause = e.getCause();
			logger.error("Exception caught: " + e.getLocalizedMessage());
			logger.error("Cause: " + cause.getLocalizedMessage());
			this.testResult.logFailure(cause);
		}

		return this.testResult;

	}

}
