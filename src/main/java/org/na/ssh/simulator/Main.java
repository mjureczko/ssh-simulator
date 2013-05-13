/**
 * Copyright (C) 2013 NetworkedAssets
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.na.ssh.simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.na.ssh.simulator.exceptions.IncorrectTestCaseDataException;

/**
 * 
 * @author Patryk Chrusciel, Bartosz Bednarek
 * 
 */
public class Main {
	
	protected SshSimulator ssh = new SshSimulator();
	
	/**
	 * Starting application from command line.
	 * 
	 * First parameter have to be specified, as path_to_test_case_file without
	 * prefix.
	 * 
	 * Additional parameters can be passed with prefix: -p port (for port
	 * number: default is 22) -h host (for host name or IP: default is
	 * localhost) -r report_port (port for report: default is 2004), if set to
	 * zero then it is turned off. Report port can be used for manipulating
	 * ssh-instance.
	 * 
	 * Built-in commands: exit, sshstatus
	 * 
	 */
	public static void main(String[] args) throws IOException, IncorrectTestCaseDataException,
			FileNotFoundException {
		
		Main m = new Main();
		m.execute(args);
		
	}
	
	protected void execute(String[] args) throws FileNotFoundException,
			IncorrectTestCaseDataException, IOException {
		
		if (args[0].startsWith("-") || args.length < 1) {
			
			System.out
					.println("Path to test case file need to be specified and it have to be the first parameter.");
			System.out
					.print("Run application with the following arguments: \n path_to_test_case_file "
							+ "\n -h hostname \n -p port  \n -r report_port\n\n " + "Example: ");
			System.out
					.print("\n> java -jar ssh-simulator-jar-with-dependencies.jar path_to_test_case_file -h host -p port\n\nfile_name");
			
			return;
		}
		
		String testCaseFile = args[0];
		
		// Later it can be moved to separated method for parsing variables into
		// list with pairs
		int port = 0;
		String host = null;
		int report_port = 2004;
		
		if (args.length > 1 && ((args.length - 1) % 2 == 0)) {
			for (int i = 1; i < args.length - 1; i++) {
				if (!args[i + 1].contains("-") && args[i].contains("-")) {
					if (args[i].equals("-p")) {
						try {
							port = Integer.parseInt(args[i + 1]);
						} catch (NumberFormatException e) {
							System.out.println("ERROR: Port have to be numeric value!");
							return;
						}
						;
					}
					if (args[i].equals("-h")) {
						host = args[i + 1];
					}
					if (args[i].equals("-r")) {
						try {
							report_port = Integer.parseInt(args[i + 1]);
						} catch (NumberFormatException e) {
							System.out.println("ERROR: Report Port have to be numeric value!");
							return;
						}
						;
					}
				}
				
			}
		} else if (!((args.length - 1) % 2 == 0) && args.length > 1) {
			System.out
					.print("\nAdditional parameters have to be used with prefix.\nEaxmple: '-p 22'  if port number is 22.");
			return;
		}
		
		File file = new File(testCaseFile);
		
		if (file.canRead())
			ssh.startSimulator(file, host, port, report_port);
		else
			System.out.println("File can not be found!");
		
	}
	
}
