package funtest.testharness.core.testcase;

import funtest.testharness.core.teststep.TestStep;

/**
 * Test case provides an implementation of the iterable interface that iterates
 * over the test step objects
 * 
 * @author codersparks
 * 
 */
public interface TestCase extends Iterable<TestStep> {

	public String getTestCaseName();
}
