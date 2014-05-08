package funtest.testharness.testcaseloader.scanner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import funtest.testharness.core.annotations.ActionMethod;
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

	protected MethodScanner() throws TestActionException {
		super();
		this.actionMethodMap = new HashMap<String, Method>();
		scan();
	}

	public Method methodForAction(String action) throws NoSuchMethodException {

		if (this.actionMethodMap.containsKey(action)) {

			return this.actionMethodMap.get(action);

		}

		throw new NoSuchMethodException(
				"No method belonging to a sub-type of TestStep exists with annotation: @TestAction(\""
						+ action + "\")");
	}

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
