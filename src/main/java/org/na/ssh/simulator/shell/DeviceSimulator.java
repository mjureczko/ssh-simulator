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

import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

/**
 * A simulator for device.
 * 
 * @author Patryk Chrusciel
 * 
 */
public class DeviceSimulator {
	
	private static final String HOST_KEY_PROVIDER = "hostkey.ser";
	
	private AbstractDeviceShellFactory shell;
	private SshServer sshd;
	
	public DeviceSimulator(AbstractDeviceShellFactory shellFactory,
			PasswordAuthenticator authenticator, String host, int port) {
		setSshd(SshServer.setUpDefaultServer());
		getSshd().setPort(port);
		getSshd().setHost(host);
		
		getSshd().setKeyPairProvider(new SimpleGeneratorHostKeyProvider(HOST_KEY_PROVIDER));
		getSshd().setPasswordAuthenticator(authenticator);
		this.setShell(shellFactory);
		getSshd().setShellFactory(shellFactory);
		
	}
	
	/**
	 * Start simulation.
	 * 
	 * @throws IOException
	 */
	public void startSimulation() throws IOException {
		getSshd().start();
	}
	
	/**
	 * Stop simulation.
	 * 
	 * @throws InterruptedException
	 */
	public void stopSimulation() throws InterruptedException {
		
		getSshd().stop();
		
	}

	/**
	 * @param shell the shell to set
	 */
	public void setShell(AbstractDeviceShellFactory shell) {
		this.shell = shell;
	}

	/**
	 * @return the shell
	 */
	public AbstractDeviceShellFactory getShell() {
		return shell;
	}

	/**
	 * @param sshd the sshd to set
	 */
	public void setSshd(SshServer sshd) {
		this.sshd = sshd;
	}

	/**
	 * @return the sshd
	 */
	public SshServer getSshd() {
		return sshd;
	}
	
}
