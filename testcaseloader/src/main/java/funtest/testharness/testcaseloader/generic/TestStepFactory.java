package funtest.testharness.testcaseloader.generic;

import java.lang.reflect.InvocationTargetException;

import funtest.testharness.core.exception.NoSuchTestStepException;
import funtest.testharness.core.teststep.TestStep;
import funtest.testharness.testcaseloader.scanner.ClassScanner;
import funtest.testharness.testcaseloader.scanner.TestStepScanner;

/**
 * Factory class that will return an instance of TestStep that is associated
 * with an action
 * 
 * @author dan-j
 * 
 */
public class TestStepFactory {

	private static ClassScanner testStepScanner;

	private static TestStepFactory factory = null;

	/**
	 * private Constructor allows Singleton instance of TestStepFactory
	 */
	private TestStepFactory() {
		try {
			testStepScanner = TestStepScanner.newClassBasedInstance();
		} catch (TestActionException e) {
			throw new RuntimeException(
					"Unable to get instance of TestStepScanner", e);
		}
	}

	/**
	 * Method to return an instance of {@link TestStepFactory}
	 * 
	 * @return an instance of {@link TestStepFactory}
	 */
	public static TestStepFactory getInstance() {
		if (factory == null) {
			factory = new TestStepFactory();
		}

		return factory;
	}

	/**
	 * <p>
	 * Factory method that will attempt to find a TestStep class that handles
	 * the action String specified
	 * </p>
	 * 
	 * <p>
	 * If such a class exists, this method will return a new instance of it,
	 * providing the class declares a zero-parameter constructor
	 * </p>
	 * 
	 * @param action
	 *            The test action for this step
	 * 
	 * @return an instance of {@link TestStep}
	 * 
	 * @throws NoSuchTestStepException
	 *             if no TestStep class can be found for the action
	 * @throws Exception
	 *             thrown if there is a problem instantiating the retrieved
	 *             Class
	 * 
	 */
	public TestStep newTestStep(String action) throws NoSuchTestStepException,
			Exception {

		Class<? extends TestStep> clazz = testStepScanner
				.clazzForAction(action);

		return clazz.getDeclaredConstructor().newInstance();

	}
}
