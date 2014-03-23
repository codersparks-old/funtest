package funtest.testharness.test.function;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.utils.function.Function;

public class Date implements Function {

	private static Log logger = LogFactory.getLog(Date.class);

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * funtest.testharness.util.function.AbstractFunction#execute(java.lang.String
	 * [])
	 */
	/**
	 * {@inheritDoc}
	 */
	public String execute(String... args) throws TestHarnessException {

		java.util.Date date = new java.util.Date();

		SimpleDateFormat formatter;

		if (args == null || args.length == 0) {
			logger.debug("No format parameter specified using default");
			formatter = new SimpleDateFormat();
		} else if (args.length == 1) {
			String format = args[0];
			if (format.length() > 0) {
				logger.debug("Format parameter specified as: " + format);
				formatter = new SimpleDateFormat(format);
			} else {
				formatter = new SimpleDateFormat();
			}
		} else {
			logger.error("Date function only takes one optional parameter, number of parameters supplied: "
					+ args.length);
			if (logger.isDebugEnabled()) {
				logger.debug("Parameters supplied:");
				for (String arg : args) {
					logger.debug("\t=> '" + arg + "'");
				}
			}
			throw new TestHarnessException(
					"Date function takes one optional parameter");
		}

		String retString = formatter.format(date);
		logger.info("Returning date: " + date);
		return retString;
	}

}
