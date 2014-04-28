package funtest.testharness.teststep.ssh;

import java.io.IOException;
import java.util.Properties;

import funtest.helperlibrary.helper.ssh.SSHHelper;
import funtest.helperlibrary.util.process.ProcessResult;
import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.teststep.AbstractTestStep;

/**
 * Run a command on a remote box via ssh
 * 
 * @author codersparks
 * 
 */
public class SSHTestStep extends AbstractTestStep {

	public SSHTestStep(String alias, Properties testStepProperties,
			TestHarnessContext context) {
		super(alias, testStepProperties, context);

	}

	@Override
	public TestResult runTestStep() {
		this.testResult.logMessage("Running test step: " + alias + " Type: "
				+ this.getClass().getName());
		// for(String key : this.properties.stringPropertyNames()) {
		// System.out.println("\t=>Property: " + key + " Value: " +
		// this.properties.getProperty(key));
		// }
		//
		// System.out.println("Alias: " + this.testResult.getAlias());

		try {

			String host = this.getProperty("host");
			this.testResult.logMessage("Host: " + host);
			String user = this.getProperty("user");
			this.testResult.logMessage("User: " + user);
			String password = this.getProperty("password");
			this.testResult.logMessage("Password: " + password);
			String command = this.getProperty("command");
			this.testResult.logMessage("Comand: " + command);

			ProcessResult processResult = SSHHelper.executeCommand(host, user,
					password, command, 10000);

			String stdout = processResult.getStdout();
			String stderr = processResult.getStderr();

			if (stdout.length() > 0) {
				this.testResult.logMessage("Standard Out from command: ");
				this.testResult.logMessage("\t" + stdout);
			}

			if (stderr.length() > 0) {
				this.testResult.logError("Standard Error from command: ");
				this.testResult.logError("\t" + stderr);
				this.testResult.logFailure("Error message detected on stderr");
			}

			if (processResult.getExitCode() == 0) {
				this.testResult.logPass("Exit code 0, test step passed");
			} else {
				this.testResult.logFailure("Exit code non zero ("
						+ processResult.getExitCode() + "), test step failed");
			}
		} catch (IOException e) {
			this.testResult.logFailure(e);
		}

		return this.testResult;
	}

}
