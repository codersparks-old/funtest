package funtest.testharness.teststep;

import java.io.IOException;
import java.util.Properties;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.teststep.TestStep;

/**
 * An implementation of {@link TestStep} that will print out the print out the
 * value of the specified variable or all variables if one is not specified
 * 
 * <p>
 * Properties expected:
 * 
 * <table border="1">
 * <tr>
 * <th>Property</th>
 * <th>Description</th>
 * <th>Required</th>
 * </tr>
 * <tr>
 * <td>result</td>
 * <td>Set the result for this test step</td>
 * <td>No - Will set to fail if not specified</td>
 * </tr>
 * <tr>
 * <td>variable</td>
 * <td>Display value of the specified variable</td>
 * <td>No - Will list all variables if not specified</td>
 * </tr>
 * </table>
 * 
 * 
 * @author codersparks
 * 
 */
public class VariablesDebugTestStep extends TestStep {

	public VariablesDebugTestStep(String alias, Properties testStepProperties,
			TestHarness testHarness) {
		super(alias, testStepProperties, testHarness);

	}

	@Override
	public TestResult runTestStep() {

		Properties harnessVariables = this.testHarness.getVariables();

		String variableName;
		
		try {
			variableName = this.getProperty("variable");
		} catch (IOException e1) {
			variableName = null;
		}

		if (variableName != null) {
			this.testResult.logMessage("Current value of " + variableName + ": " + harnessVariables.getProperty(variableName));
		} else {

			this.testResult.logMessage("Current value of properties:");

			for (String key : harnessVariables.stringPropertyNames()) {
				this.testResult.logMessage("Variable: " + key + " Value: "
						+ harnessVariables.getProperty(key));
			}
		}
		String result;
		try {
			result = this.getProperty("result");
			if (null != result && result.equalsIgnoreCase("pass")) {
				this.testResult.logPass("Setting result to pass");
			} else {
				this.testResult.logFailure("Result parameter not set to pass, setting result to fail");
			}

		} catch (IOException e) {
			this.testResult.logFailure(e);
		}

		return this.testResult;
	}

}
