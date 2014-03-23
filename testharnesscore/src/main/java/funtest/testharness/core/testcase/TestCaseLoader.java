package funtest.testharness.core.testcase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.exception.TestHarnessException;

/**
 * Provides mechanisim to load test cases using the factory specified.
 * 
 * @author codersparks
 *
 */
public class TestCaseLoader {

	private static Log logger = LogFactory.getLog(TestCaseLoader.class);
	
	private final String factoryName;
	
	private final TestCaseFactory testCaseFactory;
	
	/**
	 * Constructor for the TestCaseLoader. The loader will search for the factory class specified via factoryName parameter on the class path
	 * @param factoryName The name of the factory to load
	 * @throws TestHarnessException 
	 */
	public TestCaseLoader(String factoryName) throws TestHarnessException {
		
		logger.info("Constructing test case loader using factory: " + factoryName);
		
		this.factoryName = factoryName;
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		
		if(classLoader == null) {
			logger.debug("Could not get class loader from class using system class loader");
			classLoader = ClassLoader.getSystemClassLoader();
		}
		
		Class<?> clazz;
		try {
			
			logger.debug("Attempting to get class for class name: " + this.factoryName);
			clazz = Class.forName(this.factoryName, true, classLoader);
			
			if(TestCaseFactory.class.isAssignableFrom(clazz)) {
				logger.debug("The specified class (" + this.factoryName + ") implements TestCaseFactory therefore setting as factory");
				testCaseFactory = (TestCaseFactory) clazz.newInstance();
			} else {
				logger.error("Class: " + this.factoryName + " does not implement interface: " + TestCaseFactory.class.getName());
				throw new TestHarnessException("Class: " + this.factoryName + " does not implement interface: " + TestCaseFactory.class.getName());
			}
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException caught with message: " + e.getLocalizedMessage());
			if(logger.isDebugEnabled()) {
				logger.debug("Stacktrace: ");
				for(StackTraceElement element : e.getStackTrace()) {
					logger.debug("\t=> Class: " + element.getClassName() + " Line No: " + element.getLineNumber() + " Method: " + element.getMethodName());
				}
			}
			throw new TestHarnessException("Class Not Found Exception caught", e);
		} catch (InstantiationException e) {
			logger.error("InstantiationException caught with message: " + e.getLocalizedMessage());
			if(logger.isDebugEnabled()) {
				logger.debug("Stacktrace: ");
				for(StackTraceElement element : e.getStackTrace()) {
					logger.debug("\t=> Class: " + element.getClassName() + " Line No: " + element.getLineNumber() + " Method: " + element.getMethodName());
				}
			}
			throw new TestHarnessException("Instantiation Exception caught", e);
		} catch (IllegalAccessException e) {
			logger.error("IllegalAccessException caught with message: " + e.getLocalizedMessage());
			if(logger.isDebugEnabled()) {
				logger.debug("Stacktrace: ");
				for(StackTraceElement element : e.getStackTrace()) {
					logger.debug("\t=> Class: " + element.getClassName() + " Line No: " + element.getLineNumber() + " Method: " + element.getMethodName());
				}
			}
			throw new TestHarnessException("Illegal Access Exception caught", e);
		}
		
		logger.info("TestCaseLoader constructed");
		
	}
	
	/**
	 * Load the test steps for the specified test case
	 * @param testCaseName The name of the test case to load
	 * @param testHarness A reference to the test harness
	 * @return the test case specified
	 * @throws TestHarnessException
	 */
	public TestCase loadTestCase(String testCaseName, TestHarness testHarness) throws TestHarnessException {
		logger.info("Calling loadTestCase on " + testCaseFactory.getClass().getName());
		return testCaseFactory.loadTestCase(testCaseName, testHarness);
	}
}
