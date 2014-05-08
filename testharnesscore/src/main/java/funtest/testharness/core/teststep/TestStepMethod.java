package funtest.testharness.core.teststep;

import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.TestHarnessContext;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.result.TestResult;

public class TestStepMethod {

	private static Log log = LogFactory.getLog(TestStepMethod.class);

	private final Method actionMethod;
	private final String actionString;

	public TestStepMethod(Method actionMethod, String actionString) {
		this.actionMethod = actionMethod;
		this.actionString = actionString;
	}

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
					throw new TestHarnessException(
							"Method return type is instance of: "
									+ object.getClass().getName()
									+ ", expected: "
									+ TestResult.class.getName());
				}
			} else {
				// ... again, this should never happen due to tightness around
				// mapping actions to methods, but you never know...
				throw new TestHarnessException(
						"Declared class of @TestAction Method cannot be assigned to: "
								+ TestStep.class.getName());
			}
		} catch (Exception e) {
			String message = e.getClass().getName() + " was caught: "
					+ e.getMessage();

			log.error(message, e);

			return createFailedResult(alias, message);

		}
	}

	private TestResult createFailedResult(String alias, String message) {

		TestResult result = new TestResult(alias, this.actionString);
		result.logFailure(message);

		return result;
	}

	public String getActionString() {
		return this.actionString;
	}
}
