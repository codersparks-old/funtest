package funtest.testharness.core.utils.function;

import funtest.testharness.core.exception.TestHarnessException;

public interface Function {
	
	/**
	 * Method to actally execute the function
	 * @param args String array containing arguments for the funtion
	 * @return The String value of the function
	 */
	public String execute(String... args) throws TestHarnessException;
}
