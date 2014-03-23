/**
 * 
 */
package funtest.testharness.testfunctions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.util.function.SubString;

/**
 * @author codersparks
 *
 */
public class SubStringTest {
	
	public static String originalString;
	public static String startDelim;
	public static String endDelim;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.DEBUG);
		
		originalString = "this is a test string to demonstrate";
		startDelim = " is a ";
		endDelim = " to demo";
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
	public void testNullArgument() {
		
		SubString subString = new SubString();
		
		try {
			subString.execute((String[])null);
			fail("TestFunctionException not caught with no parameter");
		} catch (TestHarnessException e) {
			//e.printStackTrace();
		}
	}
	
	@Test
	public void testZeroLengthArgument() {
		
		String[] args = {};
		SubString subString = new SubString();
		
		try {
			subString.execute(args);
			fail("TestFunctionException not caught with zero length array");
		} catch (TestHarnessException e) {

		}
	}
	
	@Test
	public void testSingleArgument() {
		
		SubString subString = new SubString();
		
		String arg = "some arg";
		String[] args = {"Another arg"};
		
		try {
			subString.execute(arg);
			fail("TestFunctionException not caught with single String parameter");
		} catch (TestHarnessException e) {

		}
		
		try {
			subString.execute(args);
			fail("TestFunctionException not caught with single String parameter");
		} catch (TestHarnessException e) {

		}
		
	}
	
	@Test
	public void testTwinArgument() {
		String[] args = {originalString, startDelim};
		String expected = "test string to demonstrate";
		
		SubString subString = new SubString();		
		
		try {
			String retString = subString.execute(args);
			assertEquals("Generated substring not valid for two parameters", expected, retString);
			
		} catch (TestHarnessException e) {
			fail("TestFunctionException caught with message: " + e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testTripleArgument() {
		String[] args = {originalString, startDelim, endDelim};
		String expected = "test string";
		
		SubString subString = new SubString();		
		
		try {
			String retString = subString.execute(args);
			assertEquals("Generated substring not valid for two parameters", expected, retString);
			
		} catch (TestHarnessException e) {
			fail("TestFunctionException caught with message: " + e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testMultiArgument() {
		
		String[] args = {"one", "two", "three", "four"};
		SubString subString = new SubString();
		
		try {
			subString.execute(args);
			fail("TestFunctionException not caught with to many arguments");
		} catch (TestHarnessException e) {

		}
		
	}

}
