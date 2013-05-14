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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.na.ssh.simulator.reporting.commands.CommandToServer;
import org.na.ssh.simulator.reporting.messages.MessageToClient;
import org.na.ssh.simulator.reporting.messages.MessageToServer;

/**
 * @see ReportProvidingClient
 * @author Patryk Chrusciel
 * 
 */
public class ReportProvidingClientTest {
	private static final String HOST = "localhost";
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ReportProvidingClientTest.class);

	/**
	 * @throws Exception
	 * @see ReportProvidingClient#sendMessageAndGetResponse(MessageToServer)
	 */
	@Test
	public void sendMsgTest() throws Exception {
		ReportProvidingServer server = null;
		try {
			server = new ReportProvidingServer(HOST, 2004, 22, "");
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
			Assert.fail();
		}

		try {
			server.start();

			ReportProvidingClient client = null;
			try {
				client = new ReportProvidingClient(HOST, 2004);
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
				Assert.fail();
			}

			MessageToClient response = client.sendMsg(new MessageToServer(
					CommandToServer.RESET_TEST_CASE_STATUS));

			assertFalse(response.isTestCaseInProgress());
			assertTrue(response.isTestCaseSuccess());

			response = client.sendMsg(new MessageToServer(
					CommandToServer.WAS_TEST_CASE_SUCCESS));

			assertFalse(response.isTestCaseInProgress());
			assertTrue(response.isTestCaseSuccess());

		} finally {
			server.interrupt();
		}
	}

}
