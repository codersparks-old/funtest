package funtest.helperlibrary.util.process;

/** 
 * Represent the outcome of a process being executed
 * @author codersparks
 *
 */
public class ProcessResult {
	
	private String stdout;
	private String stderr;
	private Integer exitCode;
	
	/**
	 * @return the stdout
	 */
	public String getStdout() {
		return stdout;
	}
	/**
	 * @param stdout the stdout to set
	 */
	public void setStdout(String stdout) {
		this.stdout = stdout;
	}
	/**
	 * @return the stderr
	 */
	public String getStderr() {
		return stderr;
	}
	/**
	 * @param stderr the stderr to set
	 */
	public void setStderr(String stderr) {
		this.stderr = stderr;
	}
	/**
	 * @return the exitCode
	 */
	public Integer getExitCode() {
		return exitCode;
	}
	/**
	 * @param exitCode the exitCode to set
	 */
	public void setExitCode(Integer exitCode) {
		this.exitCode = exitCode;
	}
	
	
	
	

}
