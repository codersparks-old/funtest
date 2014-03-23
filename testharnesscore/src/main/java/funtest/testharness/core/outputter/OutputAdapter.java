package funtest.testharness.core.outputter;

import java.util.List;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.result.TestResult;



/**
 * <p>
 * This interface allow the development of output adaptors. These are designed
 * to take {@link TestResult} and format for output
 * 
 * <p>
 * There are two options to use, one is designed to give feedback as the test
 * case progresses and is called after each test step. The other is called at
 * the end of a test case (i.e. after all test steps have run)
 * 
 * @author codersparks
 * 
 */
public interface OutputAdapter {

	/**
	 * This will handle the full set of results and will be called at the end of
	 * the test case
	 * 
	 * @param results
	 *            A {@link List} of {@link TestResult} to process
	 * @param testHarness
	 *            A reference to allow access to the test harness
	 *            variables/properties
	 */
	public void handleResults(List<TestResult> results, TestHarness testHarness);

	/**
	 * This handles an individual {@link TestResult} and is called at the end of
	 * <b>each<\b> test step
	 * 
	 * @param result
	 *            The {@link TestResult} to be handled
	 * @param testHarness
	 *            A reference to allow access to the test harness
	 *            variables/properties
	 */
	public void handleResult(TestResult result, TestHarness testHarness);
}
