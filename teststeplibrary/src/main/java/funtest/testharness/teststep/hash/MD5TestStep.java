package funtest.testharness.teststep.hash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import funtest.helperlibrary.helper.hash.FileHashHelper;
import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.teststep.AbstractTestStep;

/**
 * Implementaion of the {@link AbstractTestStep} abstract class to validate the hashing
 * of a file
 * 
 * @author codersparks
 * 
 */
public class MD5TestStep extends AbstractTestStep {

	public MD5TestStep() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestResult runTestStep() {
		this.testResult.logMessage("Running test step: " + alias + " Type: "
				+ this.getClass().getName());

		try {

			String filename = this.getProperty("file");
			this.testResult.logMessage("File name: " + filename);
			String expectedMD5 = this.getProperty("md5sum");
			this.testResult.logMessage("Expected MD5 hash" + expectedMD5);

			File file = new File(filename);

			if (!file.isAbsolute()) {
				String workingFile = context
						.getEnvironmentProperty("workingDir")
						+ "/"
						+ context.getTestCaseName();

				File tempFile = new File(workingFile, file.getPath());
				file = tempFile;
			}
			String actualMD5 = FileHashHelper.MD5Hash(file);

			if (actualMD5.equals(expectedMD5)) {
				this.testResult
						.logPass("Generated MD5 matches expected MD5, test step passed");
			} else {
				this.testResult
						.logFailure("Generated MD5 does not match expected MD5, test step failed");
			}
		} catch (FileNotFoundException e) {
			this.testResult.logFailure(e);
		} catch (IOException e) {
			this.testResult.logFailure(e);
		}

		return this.testResult;
	}

}
