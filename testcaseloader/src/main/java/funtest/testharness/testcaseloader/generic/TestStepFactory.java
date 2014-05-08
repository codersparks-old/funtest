package funtest.testharness.testcaseloader.generic;

import java.lang.reflect.InvocationTargetException;

import funtest.testharness.core.exception.NoSuchTestStepException;
import funtest.testharness.core.teststep.TestStep;
import funtest.testharness.testcaseloader.scanner.ClassScanner;
import funtest.testharness.testcaseloader.scanner.TestStepScanner;

public class TestStepFactory {

	private static ClassScanner testStepScanner;

	private static TestStepFactory factory = null;

	private TestStepFactory() {
		try {
			testStepScanner = TestStepScanner.newClassBasedInstance();
		} catch (TestActionException e) {
			throw new RuntimeException(
					"Unable to get instance of TestStepScanner", e);
		}
	}

	public static TestStepFactory getInstance() {
		if (factory == null) {
			factory = new TestStepFactory();
		}

		return factory;
	}

	public TestStep newTestStep(String action) throws NoSuchTestStepException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {

		Class<? extends TestStep> clazz = testStepScanner
				.clazzForAction(action);

		return clazz.getDeclaredConstructor().newInstance();

	}
}
