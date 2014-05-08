package funtest.testharness.testcaseloader.generic;

import funtest.testharness.core.annotations.ActionMethod;

/**
 * Exception class to capture a method annotated with {@link ActionMethod} that
 * doesn't not conform to the rules of a {@link ActionMethod} method
 * 
 * @author dan-j
 * 
 */
public class TestActionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2653741821897105971L;

	public TestActionException() {
		super();
	}

	public TestActionException(String message) {
		super(message);
	}

}
