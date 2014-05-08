package funtest.testharness.testcaseloader.scanner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import funtest.testharness.core.annotations.Action;
import funtest.testharness.core.annotations.ActionMethod;
import funtest.testharness.core.exception.NoSuchTestStepException;
import funtest.testharness.core.result.TestResult;
import funtest.testharness.core.teststep.TestStep;
import funtest.testharness.testcaseloader.generic.TestActionException;

/**
 * TestStepScanner class that will allow all TestStep classes to be scanned for
 * {@link ActionMethod} annotations.
 * 
 * @author dan-j
 * 
 */
public class MethodScanner extends TestStepScanner {

	private final Map<String, Method> actionMethodMap;

	/**
	 * Creates a new cache of String action -> Method method and scans the classpath to populate the cache
	 * 
	 * @throws TestActionException see {@link #scan()}
	 */
	protected MethodScanner() throws TestActionException {
		super();
		this.actionMethodMap = new HashMap<String, Method>();
		scan();
	}

	/**
	 * <p>Returns a Method that belongs to a Class that implements TestStep and
	 * handles the provided action as specified by the {@link ActionMethod}
	 * annotation</p>
	 * 
	 * @param action
	 *            the action to find a Method against
	 * 
	 * @return An instance of Method that handles the provided action
	 * 
	 * @throws NoSuchMethodException
	 *             if no method can be found for the provided action
	 */
	public Method methodForAction(String action) throws NoSuchMethodException {

		if (this.actionMethodMap.containsKey(action)) {

			return this.actionMethodMap.get(action);

		}

		throw new NoSuchMethodException(
				"No method belonging to a sub-type of TestStep exists with annotation: @TestAction(\""
						+ action + "\")");
	}

	/**
	 * <p>
	 * Method that scans the Java classpath for any classes that are a sub-type
	 * of {@link TestStep} and that have methods annotated with
	 * {@link ActionMethod}
	 * </p>
	 * 
	 * <p>
	 * Any matched Methods are added to a Map with the corresponding action as
	 * the key, this allows direct lookups for the life of this class without
	 * having to rescan the classpath again
	 * </p>
	 * 
	 * @throws TestActionException
	 *             if a method belonging to a sub-type of TestStep is annotated
	 *             with {@link ActionMethod} but doesn't return
	 *             {@link TestResult}, takes zero paremeters, and if the
	 *             containing class doesn't declare a public, zero-arg
	 *             constructor
	 */
	@Override
	protected void scan() throws TestActionException {

		Reflections reflections = new Reflections(
				ClasspathHelper.forJavaClassPath());

		for (Class<? extends TestStep> clazz : reflections
				.getSubTypesOf(TestStep.class)) {

			if (!Modifier.isAbstract(clazz.getModifiers())) {

				for (Method method : clazz.getDeclaredMethods()) {

					if (method.isAnnotationPresent(ActionMethod.class)) {

						if (!method.getReturnType().equals(TestResult.class)) {

							throw new TestActionException("Method: "
									+ method.getName() + " for Class: "
									+ clazz.getName() + " does not return "
									+ TestResult.class.getName());
						}

						if (method.getParameterTypes().length != 0) {

							throw new TestActionException("Method: "
									+ method.getName() + " for Class: "
									+ clazz.getName()
									+ " should take zero parameters");
						}

						try {
							clazz.getConstructor();
						} catch (NoSuchMethodException e) {
							throw new TestActionException(
									"Class: "
											+ clazz.getName()
											+ " should declare a public, zero-args constructor");
						}

						ActionMethod testAction = method
								.getAnnotation(ActionMethod.class);

						String action = testAction.value();

						if (action != null && action.trim().length() > 0) {

							this.actionMethodMap.put(action, method);

						}
					}
				}

			}
		}
	}
}
