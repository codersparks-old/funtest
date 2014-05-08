package funtest.testharness.testcaseloader.scanner;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import funtest.testharness.core.annotations.Action;
import funtest.testharness.core.exception.NoSuchTestStepException;
import funtest.testharness.core.teststep.TestStep;
import funtest.testharness.testcaseloader.generic.TestActionException;

/**
 * <p>
 * Class that scans the classpath for all subtypes of TestStep.
 * </p>
 * 
 * <p>
 * This specific implementation looks for
 * </p>
 * 
 * @author dan-j
 * 
 */
public class ClassScanner extends TestStepScanner {

	private final Map<String, Class<? extends TestStep>> testStepMap;

	protected ClassScanner() throws TestActionException {
		super();
		this.testStepMap = new HashMap<String, Class<? extends TestStep>>();
		scan();
	}

	/**
	 * Returns a Class that implements TestStep and handles the provided action
	 * 
	 * @param action
	 *            the action to find a TestStep against
	 * 
	 * @return A class representing a subtype of TestStep
	 * 
	 * @throws NoSuchTestStepException
	 *             if no TestStep can be found for the provided action
	 */
	public Class<? extends TestStep> clazzForAction(String action)
			throws NoSuchTestStepException {
		if (this.testStepMap.containsKey(action)) {

			return this.testStepMap.get(action);

		}

		throw new NoSuchTestStepException(
				"Unable to find TestStep Class for action: " + action);
	}

	/**
	 * <p>
	 * Method that scans the Java classpath for any classes that are a sub-type
	 * of {@link TestStep} and are annotated with {@link Action}
	 * </p>
	 * 
	 * <p>
	 * Any matched classes are added to a Map with the corresponding action as
	 * the key, this allows direct lookups for the life of this class without
	 * having to rescan the classpath again
	 * </p>
	 */
	@Override
	protected void scan() throws TestActionException {

		Reflections reflections = new Reflections(
				ClasspathHelper.forJavaClassPath());

		for (Class<? extends TestStep> clazz : reflections
				.getSubTypesOf(TestStep.class)) {

			if (!Modifier.isAbstract(clazz.getModifiers())) {

				if (clazz.isAnnotationPresent(Action.class)) {
					String action = clazz.getAnnotation(Action.class).value();

					if (action == null || action.trim().length() == 0) {
						throw new TestActionException(
								"Class: "
										+ clazz.getName()
										+ " has null or empty value for @Action annotation");
					}

					this.testStepMap.put(action, clazz);
				}

			}
		}
	}

}
