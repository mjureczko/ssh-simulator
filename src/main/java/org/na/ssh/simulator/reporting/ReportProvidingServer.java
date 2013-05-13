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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.na.ssh.simulator.reporting.commands.CommandToServer;
import org.na.ssh.simulator.reporting.messages.MessageToClient;
import org.na.ssh.simulator.reporting.messages.MessageToServer;

/**
 * Thread of reporting provider
 * 
 * @author Patryk Chrusciel
 * 
 */
@Log4j
public class ReportProvidingServer extends Thread {
	@Setter
	private ServerSocket providerSocket;
	private Socket connection = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private MessageToServer msgToServer;
	private int port;
	private String bindAddress;
	
	private StringBuilder commandsErrorDetails = new StringBuilder();
	
	@Getter
	@Setter
	private String lastExecutedCommand = "";
	
	@Getter
	private boolean isTestCaseSuccess = true;
	
	@Setter
	private boolean isTestCaseInProgress = false;
	
	public int lastCommandNo = 0;
	
	private boolean haveToRun;
	private boolean isReportServerToBeRun;
	
	@Getter
	private boolean isAnyCommandError = false;
	
	private String firstErrorCommand = "";
	private String firstErrorCommandDetails = "";
	private int portSsh;
	private String testCaseFileName;
	
	/**
	 * Reset status of test case execution
	 */
	public void resetTestCase() {
		this.lastExecutedCommand = "";
		this.commandsErrorDetails = new StringBuilder();
		this.isTestCaseSuccess = true;
		this.isTestCaseInProgress = false;
		setLastCommandNo(0);
		this.isAnyCommandError = false;
		this.firstErrorCommand = "";
		this.firstErrorCommandDetails = "";
	}
	
	/**
	 * Set test case as in progress
	 */
	public void setTestCaseAsInProgress() {
		this.isTestCaseInProgress = true;
		this.isTestCaseSuccess = false;
	}
	
	public void setTestCaseEnd() {
		
		isTestCaseSuccess = !isAnyCommandError;
		isTestCaseInProgress = false;
		
		log.info(getStatusText());
		
	}
	
	public String getStatusText() {
		String status = "";
		
		status = "\n------------------------------------------------------------\nTEST STATUS\n------------------------------------------------------------\n "
				+ bindAddress
				+ " on port "
				+ portSsh
				+ "\n Test File: "
				+ testCaseFileName
				+ " \n\n FISRT COMMAND WITH ERROR: "
				+ firstErrorCommand
				+ "\n\n First Error:\n "
				+ firstErrorCommandDetails
				+ "\n\n LAST COMMAND: "
				+ lastExecutedCommand
				+ " \n\n ALL ERRORS:\n "
				+ commandsErrorDetails
				+ "\n\n Test in progress flag: "
				+ isTestCaseInProgress
				+ "\n Test Result flag:"
				+ isTestCaseSuccess
				+ "\n-------------------------------------------------------\n\n";
		
		return status;
	}
	
	/**
	 * 
	 * @param lastCommandExecutedOnSshServer
	 * @param lastCommandSuccess
	 * @param lastCommandErrorDetails
	 */
	public void addNewError(String lastExecutedCommand, String lastCommandErrorDetails) {
		if (this.firstErrorCommand.equals(""))
			firstErrorCommand = lastExecutedCommand;
		if (this.firstErrorCommandDetails.equals(""))
			firstErrorCommandDetails = lastCommandErrorDetails;
		this.isAnyCommandError = true;
		this.lastExecutedCommand = lastExecutedCommand;
		this.commandsErrorDetails = commandsErrorDetails.append(lastCommandErrorDetails
				+ "\n\n-----------------\n\n");
		this.isTestCaseSuccess = false;
	}
	
	public ReportProvidingServer(String bindAddress, int port, int portSsh, String testCaseFileName)
			throws IOException {
		msgToServer = new MessageToServer();
		this.bindAddress = bindAddress;
		this.portSsh = portSsh;
		this.port = port;
		if (port == 0)
			isReportServerToBeRun = false;
		else
			isReportServerToBeRun = true;
		this.testCaseFileName = testCaseFileName;
	}
	
	@Override
	public void run() {
		if (isReportServerToBeRun) {
			while (haveToRun) {
				try {
					// if report port is zero then it is not in use
					
					providerSocket = new ServerSocket(port, 10, InetAddress.getByName(bindAddress));
					
					log.debug("Waiting for a new connection...");
					
					connection = providerSocket.accept();
					
					log.debug("Connection received from "
							+ connection.getInetAddress().getHostName() + ".");
					
					out = new ObjectOutputStream(connection.getOutputStream());
					out.flush();
					in = new ObjectInputStream(connection.getInputStream());
					
					messagesProcessingAfterConnect();
					
				} catch (Exception e) {
					log.error("Exception: " + e.getLocalizedMessage());
					return;
				} finally {
					try {
						if (in != null) {
							in.close();
						}
						if (out != null) {
							out.close();
						}
						if (providerSocket != null) {
							providerSocket.close();
						}
					} catch (IOException e) {
						log.error("IOException : " + e.getLocalizedMessage());
					}
				}
			}
			System.out.println("REPORT SERVER STOP on port: " + port);
		}
	}
	
	private void messagesProcessingAfterConnect() throws Exception {
		
		msgToServer = (MessageToServer) in.readObject();
		if (msgToServer.getCmdToServer() == CommandToServer.RESET_TEST_CASE_STATUS) {
			log.debug("Reseting state of test case.");
			resetTestCase();
			
			sendMessage(new MessageToClient(firstErrorCommand, firstErrorCommandDetails,
					commandsErrorDetails.toString(), lastExecutedCommand, isTestCaseSuccess,
					isTestCaseInProgress));
		} else if (msgToServer.getCmdToServer() == CommandToServer.WAS_TEST_CASE_SUCCESS) {
			log.debug("Returning test case state.");
			sendMessage(new MessageToClient(firstErrorCommand, firstErrorCommandDetails,
					commandsErrorDetails.toString(), lastExecutedCommand, isTestCaseSuccess,
					isTestCaseInProgress));
		} else {
			log.error("Incorrect command from client exception.");
			throw new Exception("Incorrect command from client.");
		}
		
	}
	
	private void sendMessage(MessageToClient messageFromServer) {
		try {
			out.writeObject(messageFromServer);
			out.flush();
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	@Override
	public void start() {
		if (isReportServerToBeRun)
			log.info("REPORT SERVER START on port: " + port);
		resetTestCase();
		haveToRun = true;
		this.isTestCaseSuccess = false;
		super.start();
	}
	
	@Override
	public void interrupt() {
		haveToRun = false;
		super.interrupt();
		
	}
	
	public TestCaseExecutionInfo getTestCaseStatusDirectly() {
		TestCaseExecutionInfo info = new TestCaseExecutionInfo(firstErrorCommand,
				firstErrorCommandDetails, commandsErrorDetails.toString(), lastExecutedCommand,
				isTestCaseSuccess, isTestCaseInProgress);
		
		return info;
	}
	
	public boolean isRunning() {
		
		return haveToRun;
	}
	
	public void setHaveToFinish() {
		haveToRun = false;
		if (providerSocket != null) {
			try {
				providerSocket.close();
			} catch (IOException e) {
				// Not needed, bcs it is only trying to close.
			}
			providerSocket = null;
			try {
				finalize();
			} catch (Throwable e) {
				// Not needed, bcs it is only trying to close.
			}
		}
		try {
			super.interrupt();
		} catch (Exception e) {
			// Not needed, bcs it is only trying to close.
		}
		
	}
	
	public int getLastCommandNo() {
		return lastCommandNo;
	}
	
	public void setLastCommandNo(int lastCommandNo) {
		this.lastCommandNo = lastCommandNo;
	}
	
	/**
	 * method increases the counter by one
	 */
	public void increaeseLastCommandNo() {
		lastCommandNo++;
	}
	
}
