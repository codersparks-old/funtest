package funtest.testharness.launcher;

import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import funtest.testharness.TestHarnessImpl;
import funtest.testharness.core.TestHarness;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.testcase.TestCaseLoader;
import funtest.testoutputter.ConsoleOutputter;

public class XmlLauncher {

	/**
	 * @param args
	 * @throws TestHarnessException 
	 */
	public static void main(String[] args) throws TestHarnessException {
		
		//BasicConfigurator.configure();
		
		DOMConfigurator.configure("log4j.xml");
		
		//String testCaseName = "test.variables";
//		String testCaseName = "test.ssh";
//		String testCaseName = "test.sshDelegate";
		String testCaseName = "test.variablesdelegate";
		

		// TestCaseLoader loader = new TestCaseLoader(
		// "testharness.testcase.XMLTestCaseFactory");
		TestCaseLoader loader = new TestCaseLoader(
				"funtest.testharness.testcaseloader.xml.XMLTestCaseFactory");
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

		testHarness.setEnvironmentProperty("continueonfailure", "true");
		testHarness.addOutputter(new ConsoleOutputter());

		testHarness.loadTestCase(testCaseName);
		boolean result = testHarness.runTests();
		
		String resultString = result ? "PASS" : "FAIL";
		
		System.out.println("TestCase " + testCaseName + " completed with result: " + resultString);

	}

}
