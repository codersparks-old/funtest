package funtest.testharness.core;

import java.util.Properties;

/**
 * <p>
 * A class that provides the context for a {@link TestHarness} object.
 * 
 * <p>
 * At present this only provides the following:
 * <ul>
 * <li>Environment properties</li>
 * <li>The Test Case Name</li>
 * </ul>
 * 
 * @author codersparks
 * 
 */
public class TestHarnessContext {

	private Properties environmentalProperties = null;
	private String testCaseName;
	private final TestHarness testHarness;

	public TestHarnessContext(TestHarness testHarness) {
		this.testHarness = testHarness;
		this.environmentalProperties = new Properties();
	}

	/**
	 * Return the current environmental properties as a {@link Properties}
	 * object
	 * 
	 * @return The environmental {@link Properties}
	 */
	public Properties getEnvironmentProperties() {
		return this.environmentalProperties;
	}

	/**
	 * Get the value of the supplied environment property
	 * 
	 * @param property
	 *            The property to get
	 * @return The value of the environmental property
	 */
	public String getEnvironmentProperty(String property) {

		return this.environmentalProperties.getProperty(property);
	}

	/**
	 * Add all of the properties in the supplied properties object into the
	 * environemnt properties object
	 * 
	 * @param properties
	 */
	public void setEnvironmentalProperties(Properties properties) {
		this.environmentalProperties.putAll(properties);
	}

	/**
	 * Set and environment property
	 * 
	 * @param property
	 *            The property to set
	 * @param value
	 *            The value of the property
	 */
	public void setEnvironmentalProperty(String property, String value) {
		this.environmentalProperties.setProperty(property, value);
	}

	/**
	 * Get the test case name
	 * 
	 * @return
	 */
	public String getTestCaseName() {
		return testCaseName;
	}

	/**
	 * Set the test case name
	 * 
	 * @param testCaseName
	 *            The name of the test case
	 */
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}


	/**
	 * <p>
	 * Provides access to the test harness
	 * 
	 * <p>
	 * Used for example in the DelegateTestStep class to access the harness to
	 * delegate to another test case
	 * 
	 * @return the test harness
	 */
	public TestHarness getTestHarness() {
		return this.testHarness;
	}

}
