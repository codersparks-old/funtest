package funtest.testharness.testcaseloader.generic;

import java.lang.reflect.Method;

import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.teststep.TestStepMethod;
import funtest.testharness.testcaseloader.scanner.MethodScanner;
import funtest.testharness.testcaseloader.scanner.TestStepScanner;

public class TestStepMethodFactory {

	private static MethodScanner testStepScanner;

	private static TestStepMethodFactory factory = null;

	private TestStepMethodFactory() {
		try {
			testStepScanner = TestStepScanner.newMethodBasedInstance();
		} catch (TestActionException e) {
			throw new RuntimeException(
					"Unable to get instance of TestStepScanner", e);
		}
	}

	public static TestStepMethodFactory getInstance() {
		if (factory == null) {
			factory = new TestStepMethodFactory();
		}

		return factory;
	}

	public static TestStepMethod testStepMethodForAction(String action)
			throws TestHarnessException {
		try {

			Method testActionMethod = testStepScanner.methodForAction(action);

			return new TestStepMethod(testActionMethod, action);

		} catch (NoSuchMethodException e) {

			throw new TestHarnessException(e);

		}
	}
}
