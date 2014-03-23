package funtest.testharness.testcaseloader.xls;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.testcase.TestCase;
import funtest.testharness.core.testcase.TestCaseFactory;


public class XLSTestCaseFactory implements TestCaseFactory {

	@Override
	public TestCase loadTestCase(String testCaseName, TestHarness testHarness)
			throws TestHarnessException {
		return new XLSTestCaseImpl(testCaseName, testHarness);
	}

}
