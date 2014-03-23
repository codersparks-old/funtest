package funtest.testharness.core.teststep;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.utils.io.VariableReplaceInputStream;

/**
 * An abstract class representation of a test step
 * 
 * @author codersparks
 * 
 */
public abstract class TestStep {
	
	protected static Log logger = LogFactory.getLog(TestStep.class);

	protected final Properties properties;
	protected final String alias;
	protected final TestResult testResult;
	protected final TestHarness testHarness;

	/**
	 * The constructor for the TestStep
	 * 
	 * @param alias
	 *            The alias of the test step (allows a descriptive name to be
	 *            given for the test step)
	 * @param testStepProperties
	 *            Configuration properties for the test step 
	 * @param testHarness
	 *            The test harness calling this test step (allows access to
	 *            variables and properties)
	 */
	public TestStep(String alias, Properties testStepProperties, TestHarness testHarness) {
		String type = this.getClass().getName();
		this.alias = alias;
		logger.info("Start of constructo for alias: " + this.alias + " type: " + type);
		this.properties = testStepProperties;
		if(logger.isDebugEnabled()) {
			logger.debug("Properties set for: " + this.alias + " values: ");
			for(String name : this.properties.stringPropertyNames()) {
				logger.debug("\t=> " + name + ": '" + this.properties.getProperty(name) + "'");
			}
		}
		
		logger.debug("Creating test result for: " + alias);
		this.testResult = new TestResult(this.alias, type);
		
		this.testHarness = testHarness;
		
		logger.info("End of constructor for: " + alias);
	}
	
	protected String getProperty(String propertyName) throws IOException {
		String value = this.properties.getProperty(propertyName);
		
		if(null != value) {
			VariableReplaceInputStream inputStream = new VariableReplaceInputStream(new ByteArrayInputStream(value.getBytes()), testHarness.getVariables());
		
			StringWriter writer = new StringWriter();
		
			IOUtils.copy(inputStream, writer);
		
			return writer.toString();
		} else { 
			return null;
		}
		
	}
	
	

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}



	/**
	 * Abstract function that all implementing classes specify that is called
	 * when the test step is executed by the harness
	 * 
	 * @return
	 */
	public abstract TestResult runTestStep();
}
