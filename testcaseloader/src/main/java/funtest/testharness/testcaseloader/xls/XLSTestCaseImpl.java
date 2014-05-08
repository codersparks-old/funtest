package funtest.testharness.testcaseloader.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import funtest.testharness.core.TestHarness;
import funtest.testharness.core.exception.TestHarnessException;
import funtest.testharness.core.testcase.TestCase;
import funtest.testharness.core.teststep.TestStep;
import funtest.testharness.testcaseloader.generic.TestStepFactory;

public class XLSTestCaseImpl implements TestCase {

	private static Log logger = LogFactory.getLog(XLSTestCaseImpl.class);

	// Column maps (starting from 0)
	private static final int ALIAS_COL = 0;
	private static final int ACTION_COL = 1;
	// private static final int PROPERY_START_COL = 2;

	private Sheet sheet;
	Map<Integer, String> headerMap;

	private TestHarness testHarness;
	private String testCaseName;

	private TestStepFactory testStepFactory = TestStepFactory.getInstance();

	public XLSTestCaseImpl(String testCaseName, TestHarness testHarness)
			throws TestHarnessException {

		this.testHarness = testHarness;
		this.testCaseName = testCaseName;
		String scriptsBaseDirectory = this.testHarness.getContext()
				.getEnvironmentProperty("scriptsDir");

		String workingDirectory = this.testHarness.getContext()
				.getEnvironmentProperty("workingDir");

		if (scriptsBaseDirectory == null) {
			throw new TestHarnessException(
					"Scripts Base directory null, did you set 'scriptsDir' environment property");
		}

		if (workingDirectory == null) {
			throw new TestHarnessException(
					"Working directory null, did you set 'workignDir' environment property");
		}

		FilenameFilter filter = new RegexFileFilter(".*\\.xls$");

		File[] fileList = new File(scriptsBaseDirectory).listFiles(filter);

		try {
			for (File file : fileList) {
				Workbook workbook = new HSSFWorkbook(new FileInputStream(file));

				this.sheet = workbook.getSheet(testCaseName);

				if (this.sheet != null) {
					break;
				}
			}
		} catch (IOException e) {
			logger.error("IOException caught with message: "
					+ e.getLocalizedMessage()
					+ " Rethrowing as TestHarnessException");
			throw new TestHarnessException(e);
		}

		Row row = this.sheet.getRow(0);

		headerMap = new HashMap<Integer, String>();

		int column = 0;

		Iterator<Cell> cellIterator = row.cellIterator();

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();

			String content = cell.getStringCellValue();

			headerMap.put(column, content);

			column++;
		}

	}

	@Override
	public Iterator<TestStep> iterator() {
		return new Iterator<TestStep>() {

			int lastRowNumber = sheet.getLastRowNum();
			int rowIndex = 1;

			@Override
			public boolean hasNext() {

				if (rowIndex <= lastRowNumber) {
					return true;
				} else {
					return false;
				}
			}

			@Override
			public TestStep next() {
				TestStep testStep;
				try {

					Row row = sheet.getRow(rowIndex);

					String alias = null;
					String action = null;

					Properties parameters = new Properties();

					for (int colNo : headerMap.keySet()) {

						if (colNo == ALIAS_COL) {
							alias = row.getCell(colNo).getStringCellValue();
						} else if (colNo == ACTION_COL) {
							action = row.getCell(colNo)
													.getStringCellValue();
						} else {
							String name = headerMap.get(colNo);

							Cell cell = row.getCell(colNo);
							if (cell != null) {
								String value = cell.getStringCellValue();
								parameters.setProperty(name.toLowerCase(),
										value);
							}
						}
					}

					if (alias == null || alias.trim().length() == 0) {
						throw new TestHarnessException(
								"Alias not set for row: " + rowIndex);
					}
					
					if (action == null || action.trim().length() == 0) {
						throw new TestHarnessException("Action not set for row: " + rowIndex);
					}

					try {
						testStep = testStepFactory.newTestStep(action);
						testStep.configure(alias, testHarness.getContext(),
								parameters);
					} catch (Exception e) {
						throw new TestHarnessException(
								"Cannot create TestStep class for action: " + action, e);
					}
				} catch (Exception e) {
					logger.error("Exception of type: "
							+ e.getClass().getCanonicalName()
							+ " caught with message: "
							+ e.getLocalizedMessage()
							+ ", Returning null, Enable debug level to view stack trace");
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

				rowIndex++;
				return testStep;
			}

			@Override
			public void remove() {

			}

		};
	}

	@Override
	public String getTestCaseName() {

		return testCaseName;
	}

}
