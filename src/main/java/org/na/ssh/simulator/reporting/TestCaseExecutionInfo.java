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
package org.na.ssh.simulator.reporting;

/**
 * Info about last command execution
 * 
 * @author Patryk Chrusciel
 * 
 */
public class TestCaseExecutionInfo {

	/** AllArgsConstructor */
	public TestCaseExecutionInfo(String firstErrorCommand,
			String firstErrorCommandDetails, String commandErrorDetails,
			String lastExecutedCommand, boolean isTestCaseSuccess,
			boolean isTestCaseInProgress) {
		this.setFirstErrorCommand(firstErrorCommand);
		this.setFirstErrorCommandDetails(firstErrorCommandDetails);
		this.setCommandErrorDetails(commandErrorDetails);
		this.setLastExecutedCommand(lastExecutedCommand);
		this.setTestCaseSuccess(isTestCaseSuccess);
		this.setTestCaseInProgress(isTestCaseInProgress);
	}

	private String firstErrorCommand;
	private String firstErrorCommandDetails;
	private String commandErrorDetails;
	private String lastExecutedCommand;
	private boolean isTestCaseSuccess;
	private boolean isTestCaseInProgress;
	
	/**
	 * @param firstErrorCommand the firstErrorCommand to set
	 */
	public void setFirstErrorCommand(String firstErrorCommand) {
		this.firstErrorCommand = firstErrorCommand;
	}
	/**
	 * @return the firstErrorCommand
	 */
	public String getFirstErrorCommand() {
		return firstErrorCommand;
	}
	/**
	 * @param firstErrorCommandDetails the firstErrorCommandDetails to set
	 */
	public void setFirstErrorCommandDetails(String firstErrorCommandDetails) {
		this.firstErrorCommandDetails = firstErrorCommandDetails;
	}
	/**
	 * @return the firstErrorCommandDetails
	 */
	public String getFirstErrorCommandDetails() {
		return firstErrorCommandDetails;
	}
	/**
	 * @param commandErrorDetails the commandErrorDetails to set
	 */
	public void setCommandErrorDetails(String commandErrorDetails) {
		this.commandErrorDetails = commandErrorDetails;
	}
	/**
	 * @return the commandErrorDetails
	 */
	public String getCommandErrorDetails() {
		return commandErrorDetails;
	}
	/**
	 * @param lastExecutedCommand the lastExecutedCommand to set
	 */
	public void setLastExecutedCommand(String lastExecutedCommand) {
		this.lastExecutedCommand = lastExecutedCommand;
	}
	/**
	 * @return the lastExecutedCommand
	 */
	public String getLastExecutedCommand() {
		return lastExecutedCommand;
	}
	/**
	 * @param isTestCaseSuccess the isTestCaseSuccess to set
	 */
	public void setTestCaseSuccess(boolean isTestCaseSuccess) {
		this.isTestCaseSuccess = isTestCaseSuccess;
	}
	/**
	 * @return the isTestCaseSuccess
	 */
	public boolean isTestCaseSuccess() {
		return isTestCaseSuccess;
	}
	/**
	 * @param isTestCaseInProgress the isTestCaseInProgress to set
	 */
	public void setTestCaseInProgress(boolean isTestCaseInProgress) {
		this.isTestCaseInProgress = isTestCaseInProgress;
	}
	/**
	 * @return the isTestCaseInProgress
	 */
	public boolean isTestCaseInProgress() {
		return isTestCaseInProgress;
	}
}
