package funtest.testharness.util.function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.utils.function.Function;

/**
 * <p>
 * Implementation of {@link AbstractFunction} - Will return the substring of the
 * specified original string delimited by the given start string and end string
 * (optional)
 * 
 * <p>
 * For example given the original string "this is a test string to demonstrate"
 * and the start delimiter of "is a " with no end delimiter will return
 * "test string to demonstrate". If the end delimiter is " to demo" the function
 * will return "test string"
 * 
 * <p>
 * If no end delimiter is specified or it cannot be found then then the function
 * will return from the start delimiter to the end of the string
 * 
 * <p>
 * If the start delimiter cannot be found then the original string will be
 * returned
 * 
 * <p> Parameters:
 * 
 * <table border="1" width="50%">
 * <tr><th>Parameter number</th><th>Field</th><th>Required/Optional</th></tr>
 * <tr><td>1</td><td>Original String</td><td>Required</td></tr>
 * <tr><td>2</td><td>Start delimiter</td><td>Required</td></tr>
 * <tr><td>3</td><td>End delimiter</td><td>Optional</td></tr>
 * </table>
 *  
 * @author codersparks
 * 
 */
public class SubString implements Function {

	private static Log logger = LogFactory.getLog(SubString.class);

	public String execute(String... args) throws TestHarnessException {

		if (args == null || args.length < 2 || args.length > 3) {
			logger.error("SubString function requires parameters: <original string> <start delimiter> <end delimiter (optional)> Enable debug messages for details of parameters passed");
			if (logger.isDebugEnabled()) {
				if (args == null || args.length == 0) {
					logger.debug("Zero arguments passed to SubString function");
				} else {
					logger.debug("Arguments passed to SubString function: ");
					for (String arg : args) {
						logger.debug("\t=> '" + arg + "'");
					}
				}
			}
			throw new TestHarnessException(
					"SubString function requires parameters: <original string> <start delimiter> <end delimiter (optional)>");
		}

		String originalString = args[0];
		String startDelimString = args[1];
		String endDelimString = null;

		String retString = originalString;

		int startIndex = originalString.indexOf(startDelimString) + startDelimString.length();
		logger.debug("Index of start delimiter: " + startIndex);
		
		if(startIndex == -1) {
			logger.warn("Returning original string as start Delimiter string '" + startDelimString + "' not found in original String: " + originalString);
			return retString;
		}
		
		int endIndex = -1;

		if (args.length == 3) {
			
			logger.debug("Argument array length detected as 3, therefore processing end delimiter");
			endDelimString = args[2];
			
			endIndex = originalString.indexOf(endDelimString);

		}
		
		logger.debug("Index of end delimiter: " + endIndex);
		
		if(endIndex == -1) {
			logger.debug("End index detected as -1 therefore not using end delimiter");
			retString = originalString.substring(startIndex);
		} else {
			logger.debug("End index detected as " + endIndex);
			retString = originalString.substring(startIndex, endIndex);
		}
		
		
		logger.info("Returning string: '" + retString + "' for Substring function");
		return retString;
	}

}
