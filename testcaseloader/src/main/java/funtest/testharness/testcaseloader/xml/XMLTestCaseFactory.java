package funtest.testharness.testcaseloader.xml;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.testcase.TestCase;
import funtest.testharness.core.testcase.TestCaseFactory;

/**
 * An implementation of {@link TestCaseFactory} that loads test cases specified
 * in an xml file
 * 
 * @author codersparks
 * 
 */
public class XMLTestCaseFactory implements TestCaseFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestCase loadTestCase(String testCaseName, TestHarness harness)
			throws TestHarnessException {

		TestCase testCase = new XMLTestCaseImpl(testCaseName, harness);

		return testCase;
	}

}
