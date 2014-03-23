package funtest.testharness.core.result;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A representation of a test result and allows messages to be logged to provide
 * feedback for the progress/execution. This also allows a pass or failure to be
 * recorded (and reason for failure if it has failed)
 * 
 * @author codersparks
 * 
 */
public class TestResult {

	private static Log logger = LogFactory.getLog(TestResult.class);

	// List that will allow messages to be logged
	private List<TestResultMessage> allMessageList;
	private List<TestResultMessage> errorMessageList;

	// This is a string that gives the reason for a failure
	private String failureReason = null;

	// We set the default result to be null so that we can track if a failure or
	// a pass has been logged
	private Boolean result = null;

	private String alias;
	private String classType;

	/**
	 * Constructor for the TestResult class
	 * @param alias The name of the test step - i.e. allows a name to be given for the test step (could even be a short summary)
	 * @param actionType A string to indicate the action type
	 */
	public TestResult(String alias, String actionType) {
		this.alias = alias;
		this.classType = actionType;

		this.allMessageList = new ArrayList<TestResultMessage>();
		this.errorMessageList = new ArrayList<TestResultMessage>();
	}

	/**
	 * Log a message - This is comparable to outputting message to standard out
	 * 
	 * @param message
	 *            The message to be logged
	 */
	public void logMessage(String message) {
		TestResultMessage testResultMessage = new TestResultMessage(alias,
				message);
		allMessageList.add(testResultMessage);
		logger.trace("Message logged: " + testResultMessage.getMessage());
	}

	/**
	 * Log an error message - This is comparable to outputting message to
	 * standard error
	 * 
	 * Note this does not set the test result to be a failure
	 * 
	 * @param message
	 *            The error message to be logged
	 */
	public void logError(String message) {
		TestResultMessage testResultMessage = new TestResultMessage(alias,
				message);
		// In this case we log errors to both the error and the log list
		allMessageList.add(testResultMessage);
		errorMessageList.add(testResultMessage);
		logger.trace("Error Message logged: " + testResultMessage.getMessage());
	}

	/**
	 * <p>
	 * Log a failure - This will set the result to false (failure) and the
	 * specified message will be added to the failure reason string
	 * 
	 * <p>
	 * If a failure reason has already been logged then this failure reason is
	 * appended to the previous message and seperated by a pipe (|) character
	 * 
	 * @param message
	 *            A description of the reason for failure
	 */
	public void logFailure(String message) {
		this.logError(message);

		if (failureReason == null) {
			failureReason = message;
		} else {
			failureReason = failureReason + " | " + message;
		}

		this.result = false;
		logger.trace("Failure recored with message: " + message);
	}

	/**
	 * <p>
	 * Log a failure - This will set the result to false (failure) and message
	 * for the throwable object will be added to the failure reason string
	 * 
	 * <p>
	 * If a failure reason has already been logged then this failure reason is
	 * appended to the previous message and seperated by a pipe (|) character
	 * 
	 * <p>
	 * The stack trace will be logged as an error
	 * 
	 * @param cause
	 *            The throwable object that was caught
	 */
	public void logFailure(Throwable cause) {

		this.logFailure("Exception caught of type: "
				+ cause.getClass().getName() + " with message: "
				+ cause.getLocalizedMessage());

		this.logError("Stack trace: ");

		for (StackTraceElement element : cause.getStackTrace()) {
			this.logError(element.toString());
		}
	}

	/**
	 * <p>
	 * Log that the test has passed.
	 * 
	 * <p>
	 * If a failure has already been recorded then this call is ignored (and a
	 * warning message is logged to the harness logger)
	 * 
	 * @param message
	 *            A message that can be logged on passing
	 */
	public void logPass(String message) {

		if (!Boolean.FALSE.equals(this.result)) {
			this.logMessage(message);
			this.result = true;
		} else {
			logger.warn("Trying to set test result to passed when a failure has already been recorded. Ignoring pass message!!!");
		}

	}

	/**
	 * Get the result of the test represented by this object
	 * 
	 * @return true - Test result is a pass, false - Test result is a failure
	 */
	public boolean isPassed() {

		if (this.result == null) {
			logger.warn("Request test case result when it has not been set, returning failure...");
			return false;
		}

		return this.result.booleanValue();
	}

	/**
	 * Get the reason for failure
	 * 
	 * @return The reason for failure - Null if no failure has been recorded
	 */
	public String getFailureReason() {
		return this.failureReason;
	}

	/**
	 * This is all the messages that have been logged (Is comparable in use to
	 * Standard Out)
	 * 
	 * @return the allMessageList
	 */
	public List<TestResultMessage> getAllMessageList() {
		return allMessageList;
	}

	/**
	 * This is only the error messages that have been logged (Is comparable in
	 * use to Standard Error)
	 * 
	 * @return the errorMessageList
	 */
	public List<TestResultMessage> getErrorMessageList() {
		return errorMessageList;
	}

	/**
	 * @return the alias of the source that this message came from
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @return the classType that this result message is for
	 */
	public String getClassType() {
		return classType;
	}

}
