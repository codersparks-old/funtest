package funtest.testharness.core.teststep;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.utils.io.VariableReplaceInputStream;

/**
 * <p>
 * An abstract class representation of a test step to provide the default
 * implementation of the functions defined in {@link TestStep}
 * 
 * <p>
 * This also generates a TestResult object that the implementation can utilse in
 * variable {@link #testResult}
 * <p>
 * The remaining function {@link #runTestStep()} is the abstract function that
 * any class extending this abstract class must implement
 * 
 * @author codersparks
 * 
 */
public abstract class AbstractTestStep implements TestStep {

	protected static Log logger = LogFactory.getLog(AbstractTestStep.class);

	protected Properties properties;
	protected String alias;
	protected TestResult testResult;
	protected TestHarnessContext context;

	/**
	 * The constructor for the TestStep
	 * 
	 * @param alias
	 *            The alias of the test step (allows a descriptive name to be
	 *            given for the test step)
	 * @param testStepProperties
	 *            Configuration properties for the test step
	 * @param context
	 *            The {@link TestHarnessContext} for the TestHarness running the
	 *            test case
	 */
	public AbstractTestStep(String alias, Properties testStepProperties,
			TestHarnessContext context) {
		String type = this.getClass().getName();
		this.setAlias(alias);
		logger.info("Start of constructor for alias: " + this.alias + " type: "
				+ type);
		this.configure(testStepProperties);

		logger.debug("Creating test result for: " + alias);
		this.testResult = new TestResult(this.alias, type);

		this.context = context;

		logger.info("End of constructor for: " + alias);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Properties testStepProperties) {
		this.properties = testStepProperties;
		if (logger.isDebugEnabled()) {
			logger.debug("Properties set for: " + this.alias + " values: ");
			for (String name : this.properties.stringPropertyNames()) {
				logger.debug("\t=> " + name + ": '"
						+ this.properties.getProperty(name) + "'");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProperty(String propertyName) throws IOException {
		String value = this.properties.getProperty(propertyName);

		if (null != value) {
			VariableReplaceInputStream inputStream = new VariableReplaceInputStream(
					new ByteArrayInputStream(value.getBytes()),
					context.getEnvironmentProperties());

			StringWriter writer = new StringWriter();

			IOUtils.copy(inputStream, writer);

			return writer.toString();
		} else {
			return null;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAlias() {
		return alias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAlias(String alias) {

		if (alias == null || alias.trim().length() == 0) {
			logger.info("No alias supplied using class name");
			this.alias = this.getClass().getName();
		} else {
			this.alias = alias;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTestHarnessContext(TestHarnessContext context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract TestResult runTestStep();
}
