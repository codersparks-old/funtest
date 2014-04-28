package funtest.testharness.core;

import java.util.List;
import java.util.Properties;

import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.outputter.OutputAdapter;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.testcase.TestCaseLoader;

public interface TestHarness {

	/**
	 * This function attempts to load the test case with the specified name
	 * @param testCaseName The test case to load
	 * @throws TestHarnessException The exception thrown if the test case cannot be loaded
	 */
	public abstract void loadTestCase(String testCaseName)
			throws TestHarnessException;

	/**
	 * Run the loaded test steps
	 */
	public abstract boolean runTests();
	
	/**
	 * This function attempts to delegate flow to the specified test case loaded using the specified {@link TestCaseLoader}
	 * 
	 * @param delegateTestCaseName The name of the test case to load
	 * @param delegateTestCaseLoader The name of {@link TestCaseLoader} to load the specified test case with
	 * @throws TestHarnessException Thrown if the specifed test case cannot be loaded
	 */
	public abstract void delegateToTestCase(String delegateTestCaseName, String delegateTestCaseLoader) throws TestHarnessException;

	/**
	 * Set the value of the specified variable
	 * @param variableName The variable name to set the value for
	 * @param variableValue The value to set
	 */
	public abstract void setVariable(String variableName, String variableValue);

	/**
	 * Get the value of the specified variable
	 * @param variableName The variable to get
	 * @return The value of the variable
	 */
	public abstract String getVariable(String variableName);

	/**
	 * Put the supplied properites into variables
	 * @param properties
	 */
	public abstract void addVariables(Properties properties);

	/**
	 * 
	 * @return Variable properties
	 */
	public abstract Properties getVariables();

	/**
	 * Set the value of the specified environment property
	 * @param propertyName The property to set
	 * @param propertyValue The value of the property
	 */
	public abstract void setEnvironmentProperty(String propertyName,
			String propertyValue);

	/**
	 * Get the {@link TestHarnessContext} 
	 * @return The context for the harness
	 */
	public abstract TestHarnessContext getContext();

	/**
	 * Add the specified outputter to be used in outputting results
	 * @param outputter The outputter to be added
	 */
	public abstract void addOutputter(OutputAdapter outputter);

	/**
	 * Get the current list of outputters
	 * @return The currentList of outputters
	 */
	public abstract List<OutputAdapter> getOutputterList();

	/**
	 * @return the testCaseName
	 */
	public abstract String getTestCaseName();

	/**
	 * 
	 * @return the list of test results
	 */
	public abstract List<TestResult> getTestResults();

}