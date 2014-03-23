package funtest.testharness.core.testcase;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.exception.TestHarnessException;

/**
 * An interface to allow classes to be created that will load a list of TestSteps
 * @author codersparks
 *
 */
public interface TestCaseFactory {
	/**
	 * This function will load the specifed test case
	 * @param testCaseName The name of the test case to be loaded
	 * @param testHarness A reference to the {@link TestHarness} to allow access to variables and properties
	 * @return the loaded testcase
	 * @throws TestHarnessException Thrown if there is any error in loading the specifed test case
	 */
	public TestCase loadTestCase(String testCaseName, TestHarness testHarness) throws TestHarnessException;
}
