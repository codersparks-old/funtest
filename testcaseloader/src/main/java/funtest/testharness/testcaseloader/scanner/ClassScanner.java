package funtest.testharness.testcaseloader.scanner;

import java.lang.annotation.Annotation;
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

	public Class<? extends TestStep> clazzForAction(String action)
			throws NoSuchTestStepException {
		if (this.testStepMap.containsKey(action)) {

			return this.testStepMap.get(action);

		}

		throw new NoSuchTestStepException(
				"Unable to find TestStep Class for action: " + action);
	}

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
