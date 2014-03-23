package funtest.testharness.launcher;

import java.util.Properties;

import funtest.testharness.TestHarnessImpl;
import funtest.testharness.core.TestHarness;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.testcase.TestCaseLoader;
import funtest.testoutputter.ConsoleOutputter;

public class XlsLauncher {

	/**
	 * @param args
	 * @throws TestHarnessException 
	 */
	public static void main(String[] args) throws TestHarnessException {
		String testCaseName = "test.ssh";

		// TestCaseLoader loader = new TestCaseLoader(
		// "testharness.testcase.XMLTestCaseFactory");
		TestCaseLoader loader = new TestCaseLoader(
				"funtest.testharness.testcaseloader.xls.XLSTestCaseFactory");
		Properties variables = new Properties();
		Properties environmentProperties = new Properties();
		environmentProperties
				.setProperty("scriptsDir",
						"C:\\Users\\codersparks\\Documents\\testharness\\scripts");
		environmentProperties
				.setProperty("workingDir",
						"C:\\Users\\codersparks\\Documents\\testharness\\working");

		TestHarness testHarness = new TestHarnessImpl(loader, variables,
				environmentProperties);

		testHarness.addOutputter(new ConsoleOutputter());

		testHarness.loadTestCase(testCaseName);
		testHarness.runTests();
	}

}
