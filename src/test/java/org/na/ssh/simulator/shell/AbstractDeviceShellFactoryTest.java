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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.server.Command;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.na.ssh.simulator.exceptions.IncorrectRequestOrderException;
import org.na.ssh.simulator.exceptions.NonExistingCommandException;
import org.na.ssh.simulator.model.RequestEntryPojo;
import org.na.ssh.simulator.reporting.ReportProvidingServer;

/**
 * @see AbstractDeviceShellFactory
 * @author Patryk Chrusciel
 * 
 */
public class AbstractDeviceShellFactoryTest extends AbstractDeviceShellFactory {
	
	private static List<RequestEntryPojo> requestsForTest = new ArrayList<RequestEntryPojo>();
	
	private ReportProvidingServer serverMock;
	
	private static RequestEntryPojo request;
	
	/** */
	@BeforeClass
	public static void before() {
		request = new RequestEntryPojo("response", "prompt", 20, "command1");
		requestsForTest.add(request);
	}
	
	public AbstractDeviceShellFactoryTest() throws IOException {
		requests = requestsForTest;
	}
	
	/**
	 * @throws InterruptedException
	 * @see AbstractDeviceShellFactory#createResponse(String)
	 * 
	 * @throws NonExistingCommandException
	 */
	@Test
	public void createResponseTest() throws InterruptedException {
		serverMock = EasyMock.createMock(ReportProvidingServer.class);
		setServer(serverMock);
		
		serverMock.setTestCaseAsInProgress();
		serverMock.setLastExecutedCommand("command1");
		EasyMock.expect(serverMock.getLastCommandNo()).andReturn(0).times(1);
		serverMock.increaeseLastCommandNo();
		EasyMock.expect(serverMock.getLastCommandNo()).andReturn(1).times(3);
		EasyMock.expect(serverMock.isAnyCommandError()).andReturn(false);
		serverMock.setTestCaseEnd();
		serverMock.setLastCommandNo(-1);
		EasyMock.expect(serverMock.isTestCaseSuccess()).andReturn(true).times(1);
		//
		serverMock.setTestCaseAsInProgress();
		serverMock.setLastExecutedCommand("command2");
		EasyMock.expect(serverMock.getLastCommandNo()).andReturn(1).times(1);
		//
		serverMock.addNewError(EasyMock.eq("command2"),
				EasyMock.eq("Command 'command2' is incorrect"));
		EasyMock.replay(serverMock);
		
		try {
			String response = super.createResponse("command1");
			assertEquals("response\nprompt", response);
		} catch (NonExistingCommandException e) {
			Assert.fail();
		} catch (IncorrectRequestOrderException e) {
			Assert.fail();
		}
		
		Thread.sleep(2000);
		
		try {
			super.createResponse("command2");
		} catch (NonExistingCommandException e) {
			assertTrue(true);
		} catch (IncorrectRequestOrderException e) {
			assertTrue(true);
		}
		
		EasyMock.verify(serverMock);
	}
	
	/**
	 * @see AbstractDeviceShellFactory#getDelayInMs(String)
	 */
	@Test
	public void getTimeOutTest() {
		assertEquals(20, getDelayInMs("command1"));
	}
	
	/**
	 * @see AbstractDeviceShellFactory#create()
	 */
	@Test
	public void createTest() {
		Command command = create();
		assertTrue(command instanceof DeviceShell);
	}
}
