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

import java.io.IOException;
import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.easymock.EasyMock;

import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.na.ssh.simulator.shell.AbstractDeviceShellFactory;
import org.na.ssh.simulator.shell.DeviceSimulator;

/**
 * @see DeviceSimulator
 * @author Patryk Chrusciel
 * 
 */
public class DeviceSimulatorTest {
	/**
	 * @throws IOException
	 * @throws InterruptedException
	 * @see DeviceSimulator#startSimulation()
	 */
	@Test
	public void startSimulationTest() throws IOException, InterruptedException {
		
		SshServer serverMock = EasyMock.createMock(SshServer.class);
		serverMock.start();
		serverMock.stop();
		
		PasswordAuthenticator authenticatorMock = createMock(PasswordAuthenticator.class);
		AbstractDeviceShellFactory shellFactoryMock = createMock(AbstractDeviceShellFactory.class);
		
		String host = "host";
		DeviceSimulator ds = new DeviceSimulator(shellFactoryMock, authenticatorMock, host, 22);
		ds.setSshd(serverMock);
		
		replay(authenticatorMock, shellFactoryMock, serverMock);
		
		ds.startSimulation();
		
		ds.stopSimulation();
		
		verify(authenticatorMock, shellFactoryMock, serverMock);
	}
}
