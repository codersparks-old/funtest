package funtest.testharness.core.exception;


public class NoSuchTestStepException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3453412232143017404L;

	public NoSuchTestStepException() {
		super();
	}
	
	public NoSuchTestStepException(String message) {
		super(message);
	}
}
