package funtest.testharness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.outputter.OutputAdapter;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.testcase.TestCase;
import funtest.testharness.core.testcase.TestCaseLoader;
import funtest.testharness.core.teststep.AbstractTestStep;
import funtest.testharness.core.teststep.TestStep;

/**
 * The TestHarness class provides "the glue" of the test harness. This class
 * handles the loading of a test case, the running of each of the test steps and
 * calling the list of outputters
 * 
 * @author codersparks
 * 
 */
public class TestHarnessImpl implements TestHarness {
	private static Log logger = LogFactory.getLog(TestHarnessImpl.class);

	private List<TestResult> testResults;
	private TestCaseLoader testCaseLoader;
	private List<OutputAdapter> outputAdaptors;
	private Properties variables;
	private TestHarnessContext context;
	//private Properties environmentProperties;
	private TestCase testCase;
	private Stack<Iterator<AbstractTestStep>> iteratorStack;

	/**
	 * Constructor for the test harness class
	 * 
	 * @param loader
	 *            The loader that will be used to load the specified test case
	 * @param variables
	 *            A properties object that holds all of the variables available
	 *            for the harness
	 * @param environmentProperties
	 *            A properties object that holds all of the environmental
	 *            properties set for the harness
	 */
	public TestHarnessImpl(TestCaseLoader loader, Properties variables,
			Properties environmentProperties) {

		logger.info("Start of constructor for: " + this.getClass().getName());
		this.testCaseLoader = loader;
		this.testResults = new ArrayList<TestResult>();
		this.outputAdaptors = new ArrayList<OutputAdapter>();
		this.variables = variables;
		if (logger.isDebugEnabled()) {
			logger.debug("Variables set to: ");
			for (String name : this.variables.stringPropertyNames()) {
				logger.debug("\t=> " + name + ": "
						+ this.variables.getProperty(name));
			}
		}
		this.context = new TestHarnessContext();
		this.context.setEnvironmentalProperties(environmentProperties);
		if (logger.isDebugEnabled()) {
			logger.debug("Environment properties set to: ");
			for (String name : this.context.getEnvironmentProperties().stringPropertyNames()) {
				logger.debug("\t=> " + name + ": "
						+ this.context.getEnvironmentProperty(name));
			}
		}
		logger.info("Construction of TestHarness complete.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * funtest.testharness.core.TestHarnessInterface#loadTestCase(java.lang.String)
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadTestCase(String testCaseName) throws TestHarnessException {

		logger.info("Loading testcase: " + testCaseName);
		this.context.setTestCaseName(testCaseName);
		this.testCase = this.testCaseLoader.loadTestCase(testCaseName, this);
		// We use a stack to allow delegation
		iteratorStack = new Stack<Iterator<AbstractTestStep>>();
		iteratorStack.push(this.testCase.iterator());

		logger.debug("Loaded testcase: " + this.testCase.getTestCaseName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see funtest.testharness.core.TestHarnessInterface#runTests()
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean runTests() {
		logger.info("Running test steps");

		Iterator<AbstractTestStep> currentTestStepIterator;
		
		
		
		String continueOnFailureProperty = context.getEnvironmentProperty("continueonfailure");
		boolean continueOnFailure = Boolean.parseBoolean(continueOnFailureProperty);

		while (!iteratorStack.empty()) {
			currentTestStepIterator = iteratorStack.peek();
			TestStep testStep;
			if (currentTestStepIterator.hasNext()) {
				testStep = currentTestStepIterator.next();
				logger.debug("Running test step: " + testStep.getAlias());
				TestResult result = testStep.runTestStep();
				testResults.add(result);
				for (OutputAdapter adapter : outputAdaptors) {
					logger.debug("Handling result using: "
							+ adapter.getClass().getName());
					adapter.handleResult(result, this);
				}
				if(! (continueOnFailure || result.isPassed())) {
					break;
				}
			} else {
				// The iterator is finished (has next returned false, therefore
				// we are finished with this iterator and can therefore remove
				// from the stack
				iteratorStack.pop();
			}
		}

		for (OutputAdapter adapter : outputAdaptors) {
			logger.debug("Handling all results using: "
					+ adapter.getClass().getName());
			adapter.handleResults(testResults, this);
		}

		boolean overallResult = true;

		for (TestResult result : testResults) {
			overallResult &= result.isPassed();
		}

		logger.info("Test Steps complete with result: "
				+ (overallResult ? "PASS" : "FAIL"));

		return overallResult;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void delegateToTestCase(String delegateTestCaseName, String delegateTestCaseLoader) throws TestHarnessException {
		
		TestCaseLoader testCaseLoader;
		if(delegateTestCaseLoader == null) {
			testCaseLoader = this.testCaseLoader;
		} else {
			testCaseLoader = new TestCaseLoader(delegateTestCaseLoader);
		}
		
		TestCase testCase = testCaseLoader.loadTestCase(delegateTestCaseName, this);
		
		this.iteratorStack.push(testCase.iterator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * funtest.testharness.core.TestHarnessInterface#setVariable(java.lang.String,
	 * java.lang.String)
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVariable(String variableName, String variableValue) {
		this.variables.setProperty(variableName, variableValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * funtest.testharness.core.TestHarnessInterface#getVariable(java.lang.String)
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVariable(String variableName) {
		return this.variables.getProperty(variableName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * funtest.testharness.core.TestHarnessInterface#addVariables(java.util.Properties
	 * )
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addVariables(Properties properties) {
		this.variables.putAll(properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see funtest.testharness.core.TestHarnessInterface#getVariables()
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Properties getVariables() {
		return this.variables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * funtest.testharness.core.TestHarnessInterface#setEnvironmentProperty(java
	 * .lang.String, java.lang.String)
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnvironmentProperty(String propertyName, String propertyValue) {
		this.context.setEnvironmentalProperty(propertyName, propertyValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * funtest.testharness.core.TestHarnessInterface#getEnvironmentProperty(java
	 * .lang.String)
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestHarnessContext getContext() {
		return this.context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * funtest.testharness.core.TestHarnessInterface#addOutputter(funtest.testharness
	 * .core.core.outputter.OutputAdapter)
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addOutputter(OutputAdapter outputter) {
		logger.debug("Adding output adapter: " + outputter.getClass().getName());
		this.outputAdaptors.add(outputter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see funtest.testharness.core.TestHarnessInterface#getOutputterList()
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OutputAdapter> getOutputterList() {
		return this.outputAdaptors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see funtest.testharness.core.TestHarnessInterface#getTestCaseName()
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTestCaseName() {
		return this.context.getTestCaseName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see funtest.testharness.core.TestHarnessInterface#getTestResults()
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TestResult> getTestResults() {
		return this.testResults;
	}
}
