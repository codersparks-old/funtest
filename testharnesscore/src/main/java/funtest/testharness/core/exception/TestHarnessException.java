package funtest.testharness.core.exception;

/**
 * The base exception that will be used to signify an error has happened
 * 
 * @author codersparks
 * 
 */
public class TestHarnessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1177597357811419332L;

	/**
	 * Create an empty exception - DO NOT USE
	 */
	public TestHarnessException() {
		super();
	}

	/**
	 * Create a new exception with the specified message
	 * 
	 * @param msg
	 *            The reason the exception was raised
	 */
	public TestHarnessException(String msg) {
		super(msg);
	}

	/**
	 * Create new exception with the specified nested exception
	 * 
	 * @param cause
	 *            The nested exception
	 */
	public TestHarnessException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new exception with the specified message with a nested exception
	 * cause
	 * 
	 * @param msg
	 *            The reason the exception was raised
	 * @param cause
	 *            The nested exception
	 */
	public TestHarnessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
