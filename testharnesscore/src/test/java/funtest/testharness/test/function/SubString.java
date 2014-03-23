package funtest.testharness.test.function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.utils.function.Function;

public class SubString implements Function {

	private static Log logger = LogFactory.getLog(SubString.class);
	
	@Override
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
