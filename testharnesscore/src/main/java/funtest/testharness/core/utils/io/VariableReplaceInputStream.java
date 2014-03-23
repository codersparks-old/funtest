package funtest.testharness.core.utils.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;
import java.util.ServiceLoader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.swizzle.stream.DelimitedTokenReplacementInputStream;
import org.codehaus.swizzle.stream.StringTokenHandler;




import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.utils.function.Function;

/**
 * <p>
 * Class provides an input stream that will perform variable and function
 * replacing in a stream.
 * 
 * <p>
 * <h4>Variables</h4>
 * Variable substitution is performed by matching the begin and end delimiters
 * specified at object construction. If none are supplied then
 * {@link #DEFAULT_BEGIN_DELIM} ({@value #DEFAULT_BEGIN_DELIM}) and
 * {@link #DEFAULT_END_DELIM} ({@value #DEFAULT_END_DELIM}). The string between
 * these delimiters is then used as the key for the supplied {@link Properties}
 * object and this value will be used as the replacement. <br />
 * For example given a properties object with the following key value pair:<br />
 * <code>var1 => VARIABLE_1</code><br />
 * and original string:<br />
 * <code>String with ${var1} in it</code><br />
 * would result in string:<br />
 * <code>String with VARIABLE_1 in it</code><br />
 * 
 * <p>
 * <h4>Functions</h4> Function substitution is performed in the same way as
 * variable substitution, however to distinguish between function and variable
 * the function name will start with an underscore(_) and end with parenthesis.
 * The function name is then mapped to an implementation of the {@link Function}
 * interface and the {@link Function#execute(String...)} method is called. The
 * parameters are comma seperated and passed to the function as a String array.
 * For example we have a SubString Class that implements the {@link Function}
 * interface. This takes three parameters, the first is the original string, the
 * second is the start delimiter and the third is the end delimiter. Therefore
 * to call this we would use (using default delimiters and noting the relevance
 * of the spaces):<br />
 * <code>This is a string with ${_substring(this is a test string,this is , string)} function in it</code>
 * <br />
 * and the function would return:<br />
 * <code>a test</code><br />
 * and the InputStream would result in:<br />
 * <code>This is a string with a test function in it</code><br />
 * 
 * @author codersparks
 * 
 */
public class VariableReplaceInputStream extends InputStream {

	private static Log logger = LogFactory.getLog(VariableReplaceInputStream.class);
	
	/**
	 * The default value for the begin delimiter for a variable:
	 * {@value #DEFAULT_BEGIN_DELIM}
	 */
	public static final String DEFAULT_BEGIN_DELIM = "${";

	/**
	 * The default value for the end delimiter for a variable:
	 * {@value #DEFAULT_END_DELIM}
	 */
	public static final String DEFAULT_END_DELIM = "}";

	protected InputStream inputStream;
	protected Properties properties;
	protected String beginVarDelim;
	protected String endVarDelim;
	protected String beginFuncDelim;
	protected String endFuncDelim;
	protected boolean caseSensitive;

	/**
	 * Constructor for {@link VariableReplaceInputStream} that is case sensitive
	 * 
	 * @param inputStream
	 *            The original input stream to perform the variable replace upon
	 * @param begin
	 *            The begin delimiter
	 * @param end
	 *            The end delimiter
	 * @param properties
	 *            The properties defining the keys for the variable replacement
	 */
	public VariableReplaceInputStream(InputStream inputStream, String begin,
			String end, Properties properties) {
		this(inputStream, begin, end, properties, true);

	}

	/**
	 * Constructor for {@link VariableReplaceInputStream}
	 * 
	 * @param inputStream
	 *            The original input stream to perform the variable replace upon
	 * @param begin
	 *            The begin delimiter
	 * @param end
	 *            The end delimiter
	 * @param properties
	 *            The properties defining the keys for the variable replacement
	 * @param caseSensitive
	 *            Should the replacement be case sensitive
	 */
	public VariableReplaceInputStream(InputStream inputStream, String begin,
			String end, Properties properties, boolean caseSensitive) {
		this.beginVarDelim = begin;
		this.beginFuncDelim = begin + "_";
		this.endVarDelim = end;
		this.endFuncDelim = ")" + end;
		this.properties = properties;

		if(logger.isDebugEnabled()) {
			logger.debug("Begin variable delimiter: " + this.beginVarDelim);
			logger.debug("End variable delimiter: " + this.endVarDelim);
			logger.debug("Begin Fnction delimiter: " + this.beginFuncDelim);
			logger.debug("End function delimiter: " + this.endFuncDelim);
			
			logger.debug("Properties used for variable substitution:");
			for(String key : properties.stringPropertyNames()) {
				logger.debug("\t" + key + " => " + properties.getProperty(key));
			}
			logger.debug("End of properites");
		}
		
		InputStream temp = new DelimitedTokenReplacementInputStream(inputStream, this.beginFuncDelim,
				this.endFuncDelim, this.functionHandler, caseSensitive);
		this.inputStream = new DelimitedTokenReplacementInputStream(temp, this.beginVarDelim,
				this.endVarDelim, this.variableHandler, caseSensitive);

	}

	/**
	 * Constructor for {@link VariableReplaceInputStream} using default begin
	 * delimiter ({@value #DEFAULT_BEGIN_DELIM}) and end delimiter (
	 * {@value #DEFAULT_END_DELIM})
	 * 
	 * @param inputStream
	 *            The original input stream to perform the variable replace upon
	 * @param properties
	 *            The properties defining the keys for the variable replacement
	 */
	public VariableReplaceInputStream(InputStream inputStream,
			Properties properties) {
		this(inputStream, DEFAULT_BEGIN_DELIM, DEFAULT_END_DELIM, properties);
	}

	/**
	 * Constructor for {@link VariableReplaceInputStream} using default begin
	 * delimiter ({@value #DEFAULT_BEGIN_DELIM}) and end delimiter (
	 * {@value #DEFAULT_END_DELIM}) and {@link System#getProperties()} as the
	 * source for variable replacement
	 * 
	 * @param inputStream
	 *            The original input strea to perform the variable replace upon
	 */
	public VariableReplaceInputStream(InputStream inputStream) {
		this(inputStream, System.getProperties());
	}

	@Override
	public int read() throws IOException {
		return this.inputStream.read();
	}

	protected StringTokenHandler variableHandler = new StringTokenHandler() {

		@Override
		public String handleToken(String token) throws IOException {
			logger.info("Variable detected: " + beginVarDelim + token + endVarDelim);
			String returnValue = properties.getProperty(token);

			if (returnValue == null) {
				logger.debug("No value found for variable, returning original token");
				returnValue = beginVarDelim + token + endVarDelim;
			}

			logger.info("Variable value returned: " + returnValue);
			return returnValue;
		}
	};
	
	
	protected StringTokenHandler functionHandler = new StringTokenHandler() {
		
		@Override
		public String handleToken(String token) throws IOException {
			logger.info("Functin detected: " + beginFuncDelim + token + endFuncDelim);
			
			// Cheap test to see if token contains a variable
			if(token.contains(beginVarDelim) && token.contains(endVarDelim)) {
				logger.debug("Parsing for variables as start and end delimiters detected in function: " + beginFuncDelim + token + endFuncDelim);
				ByteArrayInputStream in = new ByteArrayInputStream(token.getBytes());
				VariableReplaceInputStream variableReplaceInputStream = new VariableReplaceInputStream(in, beginVarDelim, endVarDelim, properties, caseSensitive);
				
				StringWriter output = new StringWriter();
				IOUtils.copy(variableReplaceInputStream, output);
				token = output.toString();
				logger.info("Revised function: " + beginFuncDelim + token + endFuncDelim);
				
			}
			
			
			if(token.indexOf('(') == -1) {
				throw new IOException("Format of function should be '" + beginFuncDelim + "<func name>(<comma seperated arguments>" + endFuncDelim + " supplied: " + beginFuncDelim + token + endFuncDelim );
			}
			String[] functionTokens = token.split("\\(", 2);
			
			
			String functionName = functionTokens[0];
			String[] functionParams = functionTokens[1].split(",");
			
			if(logger.isDebugEnabled()) {
				logger.debug("Fucntion name: " + functionName);
				logger.debug("Function Params: " );
				for(String param : functionParams) {
					logger.debug("\t=> " + param);
				}
			}
			
			ServiceLoader<Function> functionServiceLoader = ServiceLoader.load(Function.class);
			
			Function function = null;
			
			for(Function functionImpl : functionServiceLoader) {
				
				if(functionName.equalsIgnoreCase(functionImpl.getClass().getSimpleName())) {
					logger.debug("Function Found: " + functionImpl.getClass().getCanonicalName());
					function = functionImpl;
					break;
				}
			}
			
			String returnString;
			
			if(function != null) {
				try {
					returnString = function.execute(functionParams);
				} catch (TestHarnessException e) {
					logger.error("Exception caught when trying to execute function: " + beginFuncDelim + token + endFuncDelim + ":" + e.getLocalizedMessage());
					throw new IOException(e);
				}
			} else {
				logger.warn("No function found for function name: " + functionName);
				returnString = beginFuncDelim + token + endFuncDelim;
			}
			
			
			
			logger.info("Returning value for function: " +returnString);
			return returnString;
		}
	};

}
