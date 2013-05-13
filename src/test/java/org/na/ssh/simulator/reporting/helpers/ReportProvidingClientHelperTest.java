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

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;
import org.na.ssh.simulator.reporting.ReportProvidingClient;
import org.na.ssh.simulator.reporting.TestCaseExecutionInfo;
import org.na.ssh.simulator.reporting.messages.MessageToClient;
import org.na.ssh.simulator.reporting.messages.MessageToServer;

/**
 * @see ReportProvidingClientHelper
 * @author Patryk Chrusciel
 * 
 */
public class ReportProvidingClientHelperTest extends ReportProvidingClientHelper {
	
	/**
	 * @throws Exception
	 * @see ReportProvidingClientHelper#getTestCaseExecutionInfo()
	 */
	@Test
	public void getTestCaseExecutionInfoTest() throws Exception {
		
		ReportProvidingClientHelper helper = new ReportProvidingClientHelper();
		
		ReportProvidingClient client = EasyMock.createMock(ReportProvidingClient.class);
		EasyMock.expect(client.sendMsg(EasyMock.anyObject(MessageToServer.class))).andReturn(
				new MessageToClient("cmd", "", "", "cmd2", true, false));
		
		EasyMock.replay(client);
		
		helper.setClient(client);
		
		TestCaseExecutionInfo info = helper.getTestCaseExecutionInfo();
		
		assertFalse(info.isTestCaseInProgress());
		assertTrue(info.isTestCaseSuccess());
		assertEquals("cmd2", info.getLastExecutedCommand());
		assertEquals("cmd", info.getFirstErrorCommand());
		assertEquals("", info.getFirstErrorCommandDetails());
		assertEquals("", info.getCommandErrorDetails());
		
		EasyMock.verify(client);
		
	}
	
}
