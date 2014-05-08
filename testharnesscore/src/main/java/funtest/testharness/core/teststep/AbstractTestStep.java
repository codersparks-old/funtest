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

	protected Properties properties = null;
	protected String alias = null;
	protected TestResult testResult = null;
	protected TestHarnessContext context = null;

	/**
	 * The harness is designed to call the default constructor which has no parameters
	 */
	public AbstractTestStep() {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(String alias, TestHarnessContext context, Properties testStepProperties) {
		
		// Set the alias 
		this.setAlias(alias);
		
		// Set the context
		this.setContext(context);
		
		this.properties = testStepProperties;
		if (logger.isDebugEnabled()) {
			logger.debug("Properties set for: " + alias + " values: ");
			for (String name : this.properties.stringPropertyNames()) {
				logger.debug("\t=> " + name + ": '"
						+ this.properties.getProperty(name) + "'");
			}
		}
		
		// Construct the test result that will store the result for the test step
		this.testResult = new TestResult(getAlias(), this.getClass().getCanonicalName());
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
	public void setContext(TestHarnessContext context) {
		this.context = context;
	}

}
