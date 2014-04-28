package funtest.testoutputter;

import java.util.List;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.outputter.OutputAdapter;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.result.TestResultMessage;

/**
 * An implementation of the {@link OutputAdapter} designed to output TestResult to the screen
 * @author codersparks
 *
 */
public class ConsoleOutputter implements OutputAdapter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResults(List<funtest.testharness.core.result.TestResult> results, TestHarness testHarness) {
		

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResult(TestResult result, TestHarness test) {

		String alias = result.getAlias();

		System.out.println("Messages for test step alias: " + alias
				+ " of type: " + result.getClassType());
		for (TestResultMessage resultMessage : result.getAllMessageList()) {
			System.out.println(
					"\t=> timestamp: " + resultMessage.getTimestamp()
					+ " [" + alias + "] " + resultMessage.getMessage());
		}
		
		if(result.getErrorMessageList().size() > 0) {
			System.err.println("Error messages for test step alias: " + alias + " of type: " + result.getClassType());
			for(TestResultMessage resultMessage : result.getErrorMessageList()) {
				System.err.println(
						"\t=> timestamp: " + resultMessage.getTimestamp()
						+ " [" + alias + "] " + resultMessage.getMessage());
			}
		}
		
		if(result.isPassed()) {
			System.out.println("\t=> Test Result: Pass");
		} else {
			System.out.println("\t=> Test Result: Fail, Reason: " + result.getFailureReason());
		}

	}
}
