/**
 * 
 */
package funtest.testharness.testfunctions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.util.function.Date;

/**
 * @author codersparks
 *
 */
public class DateTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.DEBUG);
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
	public void testZeroParameter() {
		java.util.Date expectedDate = new java.util.Date();
		try {
			String actualDateString = new Date().execute();
			SimpleDateFormat formatter = new SimpleDateFormat();
			
			String expectedDateString = formatter.format(expectedDate);
			
			assertEquals(expectedDateString, actualDateString);
		} catch (TestHarnessException e) {
			fail("TestFunctionException caught with message: " + e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testSingleParameter() {
		
		
		try {
			String dateFormat = "yyMMddHHmmssZ";
			String[] args = {dateFormat};
			java.util.Date expectedDate = new java.util.Date();
			String actualDateString = new Date().execute(args);
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			
			String expectedDateString = formatter.format(expectedDate);
			
			assertEquals(expectedDateString, actualDateString);
		} catch (TestHarnessException e) {
			fail("TestFunctionException caught with message: " + e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testMultiParameter() {
		
		String[] args = {"one","two"};
		
		Date date = new Date();
		
		try {
			date.execute(args);
			fail("TestFunctionException not caught with more than two params");
		} catch (TestHarnessException e) {
			
		}
	}

}
