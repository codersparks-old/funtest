package funtest.testharness.core.result;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that is used to represent a test result message. Allowing a source to
 * be recored for the message and also the timestamp the message was created
 * 
 * @author codersparks
 * 
 */
public class TestResultMessage {

	private static Log logger = LogFactory.getLog(TestResultMessage.class);

	private String message;
	private String source;
	private Date timestamp;

	/**
	 * Constructor for the test result message
	 * 
	 * @param message
	 *            The message to be recorded
	 */
	public TestResultMessage(String source, String message) {
		timestamp = new Date();
		this.source = source;
		this.message = message;
		logger.trace("Test result created with message: " + message
				+ " Timestamp: " + timestamp.toString());
	}

	/**
	 * Get the message Stored
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Get the timestamp that this message was created
	 * 
	 * @return
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

}
