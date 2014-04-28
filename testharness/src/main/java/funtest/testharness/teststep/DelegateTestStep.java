package funtest.testharness.teststep;

import java.util.Properties;

import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.teststep.AbstractTestStep;

/**
 * Delegate test step is designed to delegate control to another test case
 * 
 * <p>
 * Parameters expected:
 * 
 * <table border="1">
 * <tr>
 * <th>Property</th>
 * <th>Description</th>
 * <th>Required</th>
 * </tr>
 * <tr>
 * <td>testcase</td>
 * <td>The name of the test case that should be loaded</td>
 * <td>Yes</td>
 * </tr>
 * <tr>
 * <td>testcaseloader</td>
 * <td>class name of the test case loader to use to load the test case</td>
 * <td>No - If not specified test case loader used to load the original test
 * case will be used</td>
 * </tr>
 * </table>
 * 
 * @author codersparks
 * 
 */
public class DelegateTestStep extends AbstractTestStep {

	public DelegateTestStep(String alias, Properties testStepProperties,
			TestHarnessContext context) {
		super(alias, testStepProperties, context);
	}

	@Override
	public TestResult runTestStep() {
		
		try {
			String testCaseName = this.getProperty("testcase");
			if(testCaseName == null) {
				this.testResult.logFailure("Test Case Name not specified");
				return this.testResult;
			}
			
			String testCaseLoader = this.getProperty("testcaseloader");
			
			this.context.getTestHarness().delegateToTestCase(testCaseName, testCaseLoader);
			
			// no errors raised so we pass this test step
			this.testResult.logPass("No errors occured whilst loading delegated test case: " + testCaseName);
			
			return testResult;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.testResult.logFailure(e);
			return testResult;
		}
		
	}

}
