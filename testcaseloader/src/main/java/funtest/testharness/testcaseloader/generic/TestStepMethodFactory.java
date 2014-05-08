package funtest.testharness.testcaseloader.generic;

import java.lang.reflect.Method;

import funtest.testharness.core.annotations.ActionMethod;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.teststep.TestStep;
import funtest.testharness.core.teststep.TestStepMethod;
import funtest.testharness.testcaseloader.scanner.MethodScanner;
import funtest.testharness.testcaseloader.scanner.TestStepScanner;

/**
 * <p>
 * Experimental Factory class that will match actions to TestStep methods
 * annotated with {@link ActionMethod}. The factory method,
 * {@link #testStepMethodForAction(String)} should return an instance of
 * TestStepMethod if such a method exists
 * </p>
 * 
 * @author dan-j
 * 
 */
public class TestStepMethodFactory {

	private static MethodScanner testStepScanner;

	private static TestStepMethodFactory factory = null;

	/**
	 * Private contructor allows Singleton instance of the factory
	 */
	private TestStepMethodFactory() {
		try {
			testStepScanner = TestStepScanner.newMethodBasedInstance();
		} catch (TestActionException e) {
			throw new RuntimeException(
					"Unable to get instance of TestStepScanner", e);
		}
	}

	/**
	 * Static method to return the singleton instance of TestStepMethodFactory
	 * 
	 * @return instance of TestStepMethodFactory
	 */
	public static TestStepMethodFactory getInstance() {
		if (factory == null) {
			factory = new TestStepMethodFactory();
		}

		return factory;
	}

	/**
	 * <p>
	 * Factory method that returns an instance of TestStepMethod.
	 * </p>
	 * <p>
	 * This class is a wrapper around a {@link Method} that belongs to a
	 * sub-type of {@link TestStep} and is annotated with {@link ActionMethod}
	 * </p>
	 * 
	 * @param action
	 *            the action to define what method to search for
	 * @return TestStepMethod instance that wraps the matched Method
	 * 
	 * @throws TestHarnessException
	 *             if no such method handling the provided action exists
	 */
	public static TestStepMethod testStepMethodForAction(String action)
			throws TestHarnessException {
		try {

			Method testActionMethod = testStepScanner.methodForAction(action);

			return new TestStepMethod(testActionMethod, action);

		} catch (NoSuchMethodException e) {

			throw new TestHarnessException(e);

		}
	}
}
