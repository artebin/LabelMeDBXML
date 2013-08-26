package net.trevize.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SystemCommandHandler.java - Dec 9, 2008
 */

public class SystemCommandHandler {
	
	private StringBuffer sb_stderr, sb_stdout;
	
	public SystemCommandHandler(){
		sb_stderr = sb_stdout = null;
	}
	
	/**
	 * This method execute a system command and give access to the stdout and the
	 * stderr of the execution of a command. The method also return the returned
	 * error code of the command.
	 * @param cmd a String that represents the command to execute
	 * @return int the returned error code of the command
	 */
	public int exec(String cmd) {
		String line;
		int exitValue;
		sb_stdout = new StringBuffer();
		sb_stderr = new StringBuffer();

		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);

			InputStream is_err = process.getErrorStream();
			InputStreamReader isr_err = new InputStreamReader(is_err);
			BufferedReader r_err = new BufferedReader(isr_err);

			InputStream is_stdout = process.getInputStream();
			InputStreamReader isr_stdout = new InputStreamReader(is_stdout);
			BufferedReader r_stdout = new BufferedReader(isr_stdout);

			exitValue = process.waitFor();

			while ((line = r_stdout.readLine()) != null)
				sb_stdout.append(line + "\n");

			while ((line = r_err.readLine()) != null)
				sb_stderr.append(line + "\n");

		} catch (Exception ex) {
			exitValue = -1;
		}

		return exitValue;
	}
	
	/**
	 * Return the standard error output of the previous executed method.
	 * @return StringBuffer the standard error output of the previous executed method.
	 */
	public StringBuffer getStdErr(){
		return sb_stderr;
	}
	
	/**
	 * Return the standard output of the previous executed method.
	 * @return StringBuffer the standard output of the previous executed method.
	 */
	public StringBuffer getStdOut(){
		return sb_stdout;
	}
	
}
