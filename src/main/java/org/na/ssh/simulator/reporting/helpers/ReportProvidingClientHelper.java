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
package org.na.ssh.simulator.reporting.helpers;

import java.io.IOException;

import org.na.ssh.simulator.reporting.ReportProvidingClient;
import org.na.ssh.simulator.reporting.TestCaseExecutionInfo;
import org.na.ssh.simulator.reporting.commands.CommandToServer;
import org.na.ssh.simulator.reporting.messages.MessageToClient;
import org.na.ssh.simulator.reporting.messages.MessageToServer;

/**
 * 
 * @author Patryk Chrusciel
 * 
 */
public class ReportProvidingClientHelper {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ReportProvidingClientHelper.class);

	private ReportProvidingClient client;

	protected ReportProvidingClientHelper() {

	}

	/**
	 * Default to Start by Selenium (reporting port is set to zero so reporting
	 * socket is not started)
	 * 
	 * @param host
	 * @throws IOException
	 */
	public ReportProvidingClientHelper(String host) throws IOException {

		setClient(new ReportProvidingClient(host, 2004));

	}

	/**
	 * Cuntructor to use reporting server if needed
	 * 
	 * @param host
	 * @param report_port
	 * @throws IOException
	 */
	public ReportProvidingClientHelper(String host, int report_port)
			throws IOException {

		setClient(new ReportProvidingClient(host, report_port));

	}

	/**
	 * @return
	 * @throws Exception
	 * 
	 */
	public TestCaseExecutionInfo getTestCaseExecutionInfo() throws Exception {
		log.debug("Get test case execution status");
		MessageToClient msg = getClient().sendMsg(
				new MessageToServer(CommandToServer.WAS_TEST_CASE_SUCCESS));

		TestCaseExecutionInfo info = new TestCaseExecutionInfo(
				msg.getFirstErrorCommand(), msg.getFirstErrorCommandDetails(),
				msg.getCommandErrorDetails(), msg.getLastExecutedCommand(),
				msg.isTestCaseSuccess(), msg.isTestCaseInProgress());

		return info;
	}

	/**
	 * Reset test case status
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void resetTestCaseStatus() throws Exception {
		getClient().sendMsg(
				new MessageToServer(CommandToServer.RESET_TEST_CASE_STATUS));
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(ReportProvidingClient client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public ReportProvidingClient getClient() {
		return client;
	}
}
