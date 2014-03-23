/**
 * 
 */
package funtest.testharness.core.utils.io;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import funtest.testharness.core.utils.io.VariableReplaceInputStream;

/**
 * @author codersparks
 *
 */
public class VariableReplaceInputStreamTest {
	
	private static Properties testProperties;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BasicConfigurator.configure();
//		Logger.getRootLogger().setLevel(Level.OFF);
		testProperties = new Properties();
		testProperties.setProperty("var1", "%VARIABLE ONE%");
		testProperties.setProperty("var2", "%VARIABLE TWO%");
		testProperties.setProperty("test_string", "this is a test string");
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		

	}

	
	@Test
	public void singleVariableReplaceTest() throws IOException {
		String testString = "${var1}";
		String expectedString = testProperties.getProperty("var1");
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
		VariableReplaceInputStream varReplaceInputStrem = new VariableReplaceInputStream(inputStream, testProperties);
		
		StringWriter writer = new StringWriter();		
		IOUtils.copy(varReplaceInputStrem, writer);
		
		String actualString = writer.toString();
		
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void nonVariableReplaceTest() throws IOException {
		String testString = "${var3}";
		String expectedString = "${var3}";
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
		VariableReplaceInputStream varReplaceInputStrem = new VariableReplaceInputStream(inputStream, testProperties);
		
		StringWriter writer = new StringWriter();		
		IOUtils.copy(varReplaceInputStrem, writer);
		
		String actualString = writer.toString();
		
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void multiVariableReplaceTest() throws IOException {
		String testString = "${var1}${var2}${var1}";
		String expectedString = testProperties.getProperty("var1") + testProperties.getProperty("var2") + testProperties.getProperty("var1");
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
		VariableReplaceInputStream varReplaceInputStrem = new VariableReplaceInputStream(inputStream, testProperties);
		
		StringWriter writer = new StringWriter();		
		IOUtils.copy(varReplaceInputStrem, writer);
		
		String actualString = writer.toString();
		
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void functionReplaceTest() throws IOException {
		String testString = "${_substring(this is a test string,this is , string)}";
		String expectedString = "a test";
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
		VariableReplaceInputStream varReplaceInputStrem = new VariableReplaceInputStream(inputStream, testProperties);
		
		StringWriter writer = new StringWriter();		
		IOUtils.copy(varReplaceInputStrem, writer);
		
		String actualString = writer.toString();
		
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void functionContainingVariableReplaceTest() throws IOException {
		String testString = "${_substring(${test_string},this is , string)}";
		String expectedString = "a test";
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
		VariableReplaceInputStream varReplaceInputStrem = new VariableReplaceInputStream(inputStream, testProperties);
		
		StringWriter writer = new StringWriter();		
		IOUtils.copy(varReplaceInputStrem, writer);
		
		String actualString = writer.toString();
		
		assertEquals(expectedString, actualString);
	}

}
