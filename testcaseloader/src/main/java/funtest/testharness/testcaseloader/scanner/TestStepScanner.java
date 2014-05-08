package funtest.testharness.testcaseloader.scanner;

import funtest.testharness.core.annotations.ActionMethod;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.testcaseloader.generic.TestActionException;

/**
 * Abstract class that gives access to different implementations of classpath
 * scanners to map actions to {@link TestStep} classes or to action-specific Methods in a
 * {@link TestStep}
 * 
 * @author dan-j
 * 
 */
public abstract class TestStepScanner {

	/**
	 * <p>
	 * Gets an instance of {@link ClassScanner}
	 * </p>
	 * 
	 * @return instance of TestStepScanner
	 * @throws TestActionException
	 *             if an invalid method is annotated with {@link ActionMethod}
	 */
	public static ClassScanner newClassBasedInstance()
			throws TestActionException {

		return new ClassScanner();
	}

	/**
	 * <p>
	 * Gets an instance of {@link MethodScanner}
	 * </p>
	 * 
	 * @return instance of TestStepScanner
	 * @throws TestActionException
	 *             if an invalid method is annotated with {@link ActionMethod}
	 */
	public static MethodScanner newMethodBasedInstance()
			throws TestActionException {

		return new MethodScanner();
	}

	/**
	 * <p>
	 * This method scans the classpath for all non-abstract sub-types of
	 * TestStep, and extracts all methods annotated with {@link ActionMethod}. If
	 * the matched methods return {@link TestResult} and takes zero parameters,
	 * the action defined by the annotation is put into the actionMap with the
	 * method as it's value
	 * </p>
	 * 
	 * @throws TestActionException
	 *             if the method annotated with {@link ActionMethod} does not
	 *             conform to the rules of a TestStep method
	 */
	abstract protected void scan() throws TestActionException;

}
