package funtest.testharness.testcaseloader.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.testcase.TestCase;
import funtest.testharness.core.teststep.AbstractTestStep;

public class XMLTestCaseImpl implements TestCase {

	private static Map<String, String> classMap = new HashMap<String, String>();
	private static Log logger = LogFactory.getLog(XMLTestCaseImpl.class);

	static {
		classMap.put("ssh", "funtest.testharness.teststep.ssh.SSHTestStep");
		classMap.put("scp", "funtest.testharness.teststep.ssh.SCPTestStep");
		classMap.put("md5", "funtest.testharness.teststep.hash.MD5TestStep");
		classMap.put("delegate", "funtest.testharness.teststep.DelegateTestStep");
		classMap.put("variabledebug", "funtest.testharness.teststep.VariablesDebugTestStep");
	}

	private final String testCaseName;
	private final TestHarness testHarness;
	XPathExpression configXPath;
	XPathExpression actionXPath;
	XPathExpression aliasXPath;
	NodeList testStepNodeList;
	DocumentBuilder dBuilder;

	public XMLTestCaseImpl(String testCaseName, TestHarness testHarness)
			throws TestHarnessException {
		this.testCaseName = testCaseName;
		this.testHarness = testHarness;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error("ParserConfigurationException caught, rethrowing as TestHarnessException");
			throw new TestHarnessException(e);
		}

		this.initialise();
	}

	@Override
	public Iterator<AbstractTestStep> iterator() {
		// TODO Auto-generated method stub
		return new Iterator<AbstractTestStep>() {

			int index = 0;

			Properties testStepProperties = new Properties();

			@Override
			public boolean hasNext() {

				if (index < testStepNodeList.getLength()) {
					return true;
				} else {
					return false;
				}

			}

			@Override
			public AbstractTestStep next() {

				AbstractTestStep testStep;

				try {

					// First we clear the properties (this save creating a new
					// properties object)
					testStepProperties.clear();

					Document nodeDoc = dBuilder.newDocument();

					Node node = nodeDoc.importNode(
							testStepNodeList.item(index), true);
					nodeDoc.appendChild(node);

					String actionString = (String) actionXPath.evaluate(
							nodeDoc, XPathConstants.STRING);
					String aliasString = (String) aliasXPath.evaluate(nodeDoc,
							XPathConstants.STRING);

					NodeList configNodes = (NodeList) configXPath.evaluate(
							nodeDoc, XPathConstants.NODESET);
					for (int j = 0; j < configNodes.getLength(); j++) {
						Node configNode = configNodes.item(j);

						String name = configNode.getNodeName();
						String value = configNode.getTextContent();
						// System.out.println("\t=>Name: " + name + " Value: " +
						// value);
						testStepProperties.setProperty(name, value);
					}
					
					String className = classMap.get(actionString.toLowerCase());

					if(null == className) {
						logger.error("Classmap does not contain action: " + actionString);
						if(logger.isDebugEnabled()) {
							logger.debug("Available actions: ");
							for(String classMapKey : classMap.keySet()) {
								logger.debug("\t=> " + classMapKey + " loads class: " + classMap.get(classMapKey));
							}
						}
						throw new TestHarnessException("Cannot find class for action: " + actionString);
					}
					
					Class<?> clazz = this.getClass().getClassLoader()
							.loadClass(className);
					
					

					if (AbstractTestStep.class.isAssignableFrom(clazz)) {
						testStep = (AbstractTestStep) clazz.getDeclaredConstructor(
								String.class, Properties.class,
								TestHarness.class).newInstance(aliasString,
								testStepProperties, testHarness);

					} else {
						throw new TestHarnessException(
								"Cannot load class using action: "
										+ actionString);
					}

				} catch (Exception e) {
					logger.error("Exception of type: "
							+ e.getClass().getCanonicalName()
							+ " with message: "
							+ e.getLocalizedMessage()
							+ " Returning null, Enable debug for stack trace messages");

					if (logger.isDebugEnabled()) {
						logger.debug("Stack Trace: ");
						for (StackTraceElement element : e.getStackTrace()) {
							logger.debug("\t=> " + element.getClassName() + "."
									+ element.getMethodName() + "("
									+ element.getFileName() + ":"
									+ element.getLineNumber() + ")");
						}
					}

					testStep = null;
				}

				// Increase the index tracking what item we are currently
				// working on
				index++;
				return testStep;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"Remove is not supported for test step iterator");

			}

		};
	}

	@Override
	public String getTestCaseName() {
		return testCaseName;
	}

	private void initialise() throws TestHarnessException {

		String scriptsBaseDirectory = testHarness.getContext()
				.getEnvironmentProperty("scriptsDir");
		//String schemaLocation = testHarness
		//		.getEnvironmentProperty("schemaLocation");
		String workingDirectory = testHarness.getContext()
				.getEnvironmentProperty("workingDir");

		if (scriptsBaseDirectory == null) {
			throw new TestHarnessException(
					"Scripts Base directory null, did you set 'scriptsDir' environment property");
		}

		// if(schemaLocation == null) {
		// throw new
		// TestHarnessException("Schema location null, did you set 'schemaLocation' environment property");
		// }

		if (workingDirectory == null) {
			throw new TestHarnessException(
					"Working directory null, did you set 'workignDir' environment property");
		}

		try {
			// Load variables allows variables to be loaded when test case is
			// loaded
			loadVariables(scriptsBaseDirectory, testCaseName);

			File testCaseFile = generateTestCaseFile(scriptsBaseDirectory,
					testCaseName);

			InputStream inputStream = new FileInputStream(testCaseFile);
			Document doc = dBuilder.parse(inputStream);

			XPathFactory xpathFactory = XPathFactory.newInstance();

			testStepNodeList = (NodeList) xpathFactory.newXPath().evaluate(
					"/testcase/teststep", doc, XPathConstants.NODESET);

			logger.debug("Node count: " + testStepNodeList.getLength());

			configXPath = xpathFactory.newXPath().compile("/teststep/*");
			actionXPath = xpathFactory.newXPath().compile("/teststep/@action");
			aliasXPath = xpathFactory.newXPath().compile("/teststep/@alias");

			/*
			 * for (int i = 0; i < testStepNodes.getLength(); i++) {
			 * 
			 * Properties testStepProperties = new Properties();
			 * 
			 * 
			 * 
			 * 
			 * 
			 * }
			 */

		} catch (Exception e) {

			throw new TestHarnessException("Exception caught of type: "
					+ e.getClass().getName() + " Message: "
					+ e.getLocalizedMessage(), e);
		}

	}

	private File generateTestCaseFile(String scriptsBaseDirectory,
			String testCaseName) {

		File baseDir = new File(scriptsBaseDirectory, testCaseName.replaceAll(
				"\\.", "/"));

		return new File(baseDir, "action.xml");
	}

	private void loadVariables(String scriptsBase, String testCaseName)
			throws FileNotFoundException, IOException {

		Properties properties = new Properties();

		String variableFileName = "variables.properties";

		File scriptsBaseDirectory = new File(scriptsBase);

		File baseVariableFile = new File(scriptsBaseDirectory, variableFileName);

		if (baseVariableFile.canRead()) {
			properties.load(new FileInputStream(baseVariableFile));
		}

		String[] testCaseNameTokens = testCaseName.split("\\.");

		File currentDir = scriptsBaseDirectory;

		for (String token : testCaseNameTokens) {

			currentDir = new File(currentDir, token);

			File variableFile = new File(currentDir, variableFileName);
			if (variableFile.canRead()) {
				properties.load(new FileInputStream(variableFile));
			}
		}

		this.testHarness.addVariables(properties);

	}
}
