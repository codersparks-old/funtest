package funtest.testharness.core.teststep;

import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.annotations.ActionMethod;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.result.TestResult;

/**
 * <p>
 * Experimental class that wraps a Method to be executed by the test harness
 * </p>
 * 
 * <p>
 * The creation of this was to enable the idea of annotating methods with
 * {@link ActionMethod} to bind actions to methods. The test harness would use
 * TestStepMethodFactory to retrieve an instance of this class that is
 * associated with a specific action. The the harness would then call
 * {@link #runActionMethod(String, TestHarnessContext, Properties)} on this
 * class to run the test step.
 * </p>
 * 
 * @author dan-j
 * 
 */
public class TestStepMethod {

	private static Log log = LogFactory.getLog(TestStepMethod.class);

	private final Method actionMethod;
	private final String actionString;

	/**
	 * Constructor that defines the method this test step should execute, and
	 * which method it is associated with
	 * 
	 * @param actionMethod
	 *            The method that handles the action
	 * @param actionString
	 *            The action that defines this step
	 */
	public TestStepMethod(Method actionMethod, String actionString) {
		this.actionMethod = actionMethod;
		this.actionString = actionString;
	}

	/**
	 * This method should be called by TestHarness when running the test to
	 * execute the test step.
	 * 
	 * @param alias
	 *            the alias of the test step
	 * @param context
	 *            the context of the TestHarness
	 * @param properties
	 *            all the properties defined for this step
	 * @return a TestResult representing the result of this test step
	 */
	public TestResult runActionMethod(String alias, TestHarnessContext context,
			Properties properties) {

		try {
			Class<?> testStepClazz = this.actionMethod.getDeclaringClass();

			if (TestStep.class.isAssignableFrom(testStepClazz)) {

				TestStep testStep = (TestStep) testStepClazz.newInstance();
				testStep.configure(alias, context, properties);

				Object object = this.actionMethod.invoke(testStep);

				if (object instanceof TestResult) {
					return (TestResult) object;
				} else {
					// this should never happen due to tightness around mapping
					// actions to methods, but you never know...
					String message = "Method return type is instance of: "
							+ object.getClass().getName() + ", expected: "
							+ TestResult.class.getName();

					log.error(message);

					return createFailedResult(alias, message);
				}
			} else {
				// ... again, this should never happen due to tightness around
				// mapping actions to methods, but you never know...
				String message = "Declared class of @TestAction Method cannot be assigned to: "
						+ TestStep.class.getName();

				log.error(message);

				return createFailedResult(alias, message);
			}
		} catch (Exception e) {

			log.error(e.getMessage(), e);

			return createFailedResult(alias, e);

		}
	}

	/**
	 * Helper method used by
	 * {@link #runActionMethod(String, TestHarnessContext, Properties)} to
	 * create a failed TestResult for the test step named 'alias' with the
	 * specified error message
	 * 
	 * @param alias
	 * @param message
	 * @return
	 */
	private TestResult createFailedResult(String alias, String message) {

		TestResult result = new TestResult(alias, this.actionString);
		result.logFailure(message);
		return result;
	}

	/**
	 * Helper method used by
	 * {@link #runActionMethod(String, TestHarnessContext, Properties)} to
	 * create a failed TestResult for the test step named 'alias' with the
	 * {@link Throwable} that caused the error
	 * 
	 * @param alias
	 * @param cause
	 * @return
	 */
	private TestResult createFailedResult(String alias, Throwable cause) {

		TestResult result = new TestResult(alias, this.actionString);
		result.logFailure(cause);
		return result;
	}

	public String getActionString() {
		return this.actionString;
	}
}
