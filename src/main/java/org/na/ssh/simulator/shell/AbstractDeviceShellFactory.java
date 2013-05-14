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
package org.na.ssh.simulator.shell;

import java.io.IOException;
import java.util.List;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;
import org.na.ssh.simulator.control.RequestEntryControl;
import org.na.ssh.simulator.exceptions.IncorrectRequestOrderException;
import org.na.ssh.simulator.exceptions.NonExistingCommandException;
import org.na.ssh.simulator.model.RequestEntryPojo;
import org.na.ssh.simulator.reporting.ReportProvidingServer;

/**
 * Abstract class for router schell factory.
 * 
 * @author Patryk Chrusciel
 * 
 */
public abstract class AbstractDeviceShellFactory implements Factory<Command> {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
	.getLogger(AbstractDeviceShellFactory.class);
	private static final String EXCLAMATION_MARK = "!";
	
	protected List<RequestEntryPojo> requests;
	private String lastPrompt = "";
	protected String host;
	private ReportProvidingServer server;
	private RequestEntryControl requestEntryControl = new RequestEntryControl();
	
	/**
	 * Protected constructor for test purposes
	 * 
	 */
	protected AbstractDeviceShellFactory() {
	}
	
	public AbstractDeviceShellFactory(List<RequestEntryPojo> requests, String host,
			int report_port, int port, String testCaseFileName) throws IOException {
		this.requests = requests;
		
		log.debug("Commands list: ");
		for (RequestEntryPojo r : requests) {
			log.debug(" " + r.getCommand());
		}
		
		this.host = host;
		try {
			setServer(new ReportProvidingServer(host, report_port, port, testCaseFileName));
			getServer().start();
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
	}
	
	@Override
	public Command create() {
		
		log.debug("Creating new connection instance...");
		
		return new DeviceShell(this);
	}
	
	protected String createResponse(String command) throws IncorrectRequestOrderException,
			NonExistingCommandException {
		getServer().setTestCaseAsInProgress();
		
		log.debug("create response for command: " + command);
		log.debug("request list size: " + requests.size());
		
		if (command.equals(EXCLAMATION_MARK)) {
			return command + getLastPrompt();
		}
		
		getServer().setLastExecutedCommand(command);
		checkIfCommandExists(command);
		
		RequestEntryPojo request = handleCommand(command);
		return request.getResponse() + request.getPrompt();
	}
	
	private RequestEntryPojo handleCommand(String command) throws IncorrectRequestOrderException {
		
		getServer().increaeseLastCommandNo();
		
		RequestEntryPojo request = checkCommandOrder(command);
		afterTestCaseEndWithOrderCheck();
		return request;
	}
	
	private void afterTestCaseEndWithOrderCheck() {
		if (requests.size() == getServer().getLastCommandNo()) {
			
			getServer().setTestCaseEnd();
			getServer().setLastCommandNo(-1);
			log.info("Setting as test case end... with result: " + getServer().isTestCaseSuccess());
		} else {
			log.debug("It is not the end of test...");
		}
	}
	
	private RequestEntryPojo checkCommandOrder(String currentCommand)
			throws IncorrectRequestOrderException {
		
		RequestEntryPojo request;
		try {
			log.debug("command order number is: " + getServer().getLastCommandNo());
			log.debug("request list size: " + requests.size());
			
			request = requests.get(getServer().getLastCommandNo() - 1);
			setLastPrompt(request.getPrompt());
			
		} catch (IndexOutOfBoundsException e) {
			// When checking commands order, commands will be sended till the
			// end, so in defined order check then IndexOutOfBounds will be
			// given after expected number of defined commands. So it will give
			// info that test is finished but was incorrect. There can be
			// situation that client will provide error checking in command line
			// by him self so he can then continue bcs last command succesfully
			// is provided and he will not see errors later, however test result
			// will be given incorrect bcs one error had occure.
			getServer().addNewError(currentCommand,
					"There was too much commands or incorrect command used in overall test with command order check.");
			
			if (getServer().getLastCommandNo() != 0) {
				// it will call it only in first index out of bounds when
				// command have been to much
				getServer().setTestCaseEnd();
			}
			
			getServer().setLastCommandNo(-1);
			getServer().setTestCaseInProgress(false);
			
			throw new IncorrectRequestOrderException();
			
		}
		
		if (!request.matches(currentCommand)) {
			getServer().addNewError(currentCommand, "Command order is incorrect. Was '" + currentCommand
					+ "', should be '" + request.getCommand() + "'");
			
			afterTestCaseEndWithOrderCheck();
			
			throw new IncorrectRequestOrderException();
		}
		
		// If elswhere previously command order was not ok so don't bother to
		// wait but only rest speed order check.
		if (getServer().isAnyCommandError()) {
			getServer().addNewError(currentCommand, "Command: '" + currentCommand
					+ "' is in correct place. But previusly where incorrect placement of commands.");
			
			afterTestCaseEndWithOrderCheck();
			
			throw new IncorrectRequestOrderException();
		}
		
		log.debug("Command is in correct place.");
		
		return request;
	}
	
	private void checkIfCommandExists(String command) throws NonExistingCommandException {
		log.debug("check existing of command: " + command);
		
		if (getServer().getLastCommandNo() == -1)
			getServer().setTestCaseInProgress(false);
		
		if (requestEntryControl.searchByCommand(requests, command) == null) {
			getServer().addNewError(command, "Command '" + command + "' is incorrect");
			throw new NonExistingCommandException();
		}
	}
	
	/**
	 * Checks the time of delay (in ms) that will be used in generating response
	 * in answer to specified command
	 * 
	 * @param command
	 *            Command to be checked for delay in response
	 * @return time Time in Miliseconds that will be used for generate the
	 *         reponse for the command.
	 */
	protected long getDelayInMs(String command) {
		
		RequestEntryPojo response = requestEntryControl.searchByCommand(requests, command);
		if (response != null) {
			return response.getDelayInMs();
		}
		return 0;
	}
	
	public ReportProvidingServer getServer() {
		
		return server;
	}

	/**
	 * @param lastPrompt the lastPrompt to set
	 */
	public void setLastPrompt(String lastPrompt) {
		this.lastPrompt = lastPrompt;
	}

	/**
	 * @return the lastPrompt
	 */
	public String getLastPrompt() {
		return lastPrompt;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(ReportProvidingServer server) {
		this.server = server;
	}
	
}
