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

import lombok.Getter;
import lombok.Setter;
import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.na.ssh.simulator.shell.AbstractDeviceShellFactory;

/**
 * A simulator for device.
 * 
 * @author Patryk Chrusciel
 * 
 */
public class DeviceSimulator {
	
	private static final String HOST_KEY_PROVIDER = "hostkey.ser";
	
	@Getter
	private AbstractDeviceShellFactory shell;
	
	@Getter
	@Setter
	protected SshServer sshd;
	
	public DeviceSimulator(AbstractDeviceShellFactory shellFactory,
			PasswordAuthenticator authenticator, String host, int port) {
		sshd = SshServer.setUpDefaultServer();
		sshd.setPort(port);
		sshd.setHost(host);
		
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(HOST_KEY_PROVIDER));
		sshd.setPasswordAuthenticator(authenticator);
		this.shell = shellFactory;
		sshd.setShellFactory(shellFactory);
		
	}
	
	/**
	 * Start simulation.
	 * 
	 * @throws IOException
	 */
	public void startSimulation() throws IOException {
		sshd.start();
	}
	
	/**
	 * Stop simulation.
	 * 
	 * @throws InterruptedException
	 */
	public void stopSimulation() throws InterruptedException {
		
		sshd.stop();
		
	}
	
}
