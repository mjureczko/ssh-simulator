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
package org.na.ssh.simulator.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

/**
 * Entry for response
 * 
 * @author Patryk Chrusciel
 * 
 */

public class RequestEntryPojo {
	
	@Getter
	private String command;
	
	/**
	 * Setter
	 */
	public void setCommand(String command) {
		this.command = command;
		createCommandPattern(command);
	}
	
	@Getter
	@Setter
	private String response;
	@Getter
	@Setter
	private String prompt;
	@Getter
	@Setter
	private long delayInMs;
	
	private Pattern commandPattern;
	
	/**
	 * 
	 * @param response
	 *            Response message.
	 * @param prompt
	 *            Prompt message without '\n' sign on begin and '#' on end.
	 * @param delayInMs
	 *            How much time should host wait for response. Value given in
	 *            ms.
	 */
	public RequestEntryPojo(String response, String prompt, long delayInMs, String command) {
		this.command = command;
		this.response = response;
		this.delayInMs = delayInMs;
		if (prompt == null || prompt.equals("") || prompt.length() == 1) {
			this.prompt = "\n #";
		} else {
			this.prompt = "\n" + prompt;
		}
		createCommandPattern(command);
	}
	
	/**
	 * 
	 * @param response
	 *            Response message.
	 * @param prompt
	 *            Prompt message without '\n' sign on begin and '#' on end.
	 */
	public RequestEntryPojo(String response, String prompt, String command) {
		this(response, prompt, 0, command);
	}
	
	private void createCommandPattern(String command) {
		try {
			commandPattern = Pattern.compile(command);
		} catch (Exception e) {
			commandPattern = null;
		}
	}
	
	/**
	 * checks if command matches the response command or response command regex
	 * 
	 */
	public boolean matches(String command) {
		if (command == null) {
			return false;
		}
		if (command.equals(this.command)) {
			return true;
		}
		
		if (commandPattern != null) {
			Matcher matcher = commandPattern.matcher(command);
			return matcher.matches();
		}
		return false;
	}
}
