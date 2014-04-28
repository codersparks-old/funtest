package funtest.testharness.core.teststep;

import java.io.IOException;
import java.util.Properties;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.result.TestResult;

/**
 * The methods that must be implemented by
 * 
 * @author codersparks
 * 
 */
public interface TestStep {

	/**
	 * The configure method allow the loader of the {@link TestStep} to
	 * configure the teststep
	 * 
	 * @param propeties
	 *            The {@link Properties} object that contains the required
	 *            parameters to configure the {@link TestStep}
	 */
	public void configure(Properties propeties);

	/**
	 * <p>
	 * A test step is configured using a {@link Properties} object specified via
	 * the {@link #configure(Properties)} method.
	 * 
	 * <p>
	 * This method allows access to the property defined in the supplied
	 * {@link Properties} object
	 * 
	 * @param propertyName
	 *            The name of the property to get
	 * @return The value of the property
	 * @throws IOException
	 */
	public String getProperty(String propertyName) throws IOException;

	/**
	 * Get the alias for the TestStep
	 * 
	 * @return the alias
	 */
	public String getAlias();

	/**
	 * Set the alias for the TestStep
	 * 
	 * @param alias
	 *            The alias to use for the test step
	 */
	public void setAlias(String alias);

	/**
	 * Supply the TestStep implementation with the {@link TestHarnessContext} to
	 * allow access to the harness level properties (such as environment)
	 * 
	 * @param context The {@link TestHarnessContext} to use
	 */
	public void setTestHarnessContext(TestHarnessContext context);

	/**
	 * Abstract function that all implementing classes specify that is called
	 * when the test step is executed by the harness (The actual function called
	 * by the {@link TestHarness})
	 * 
	 * @return
	 */
	public TestResult runTestStep();

}