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

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.text.StyleConstants.CharacterConstants;

import lombok.extern.log4j.Log4j;

import org.apache.sshd.server.ExitCallback;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.na.ssh.simulator.exceptions.IncorrectRequestOrderException;
import org.na.ssh.simulator.exceptions.NonExistingCommandException;

/**
 * @see DeviceShellOLD
 * @author Patryk Chrusciel
 * 
 */
@Log4j
public class DeviceShellTest {
	/**
	 * {@link DeviceShell}
	 * 
	 * @throws IOException
	 * @throws NonExistingCommandException
	 * @throws IncorrectRequestOrderException
	 * @throws InterruptedException
	 */
	@Test
	public void runTest() throws IOException, NonExistingCommandException,
			IncorrectRequestOrderException, InterruptedException {
		String cmd = "command";
		String msg = "message";
		AbstractDeviceShellFactory abstractDeviceShellFactory = EasyMock
				.createMock(AbstractDeviceShellFactory.class);
		
		EasyMock.expect(abstractDeviceShellFactory.createResponse(cmd)).andReturn((msg));
		EasyMock.expect(abstractDeviceShellFactory.getDelayInMs(cmd)).andReturn(0l);
		
		EasyMock.replay(abstractDeviceShellFactory);
		
		DeviceShell ds = new DeviceShell(abstractDeviceShellFactory);
		
		OutputStream os = new ByteArrayOutputStream();
		InputStream is = new ByteArrayInputStream((cmd + "\n").getBytes());
		
		ds.setInputStream(new BufferedInputStream(is));
		ds.setOutputStream(os);
		ds.setExitCallback(new ExitCallback() {
			
			@Override
			public void onExit(int exitValue, String exitMessage) {
				log.info("On exit");
			}
			
			@Override
			public void onExit(int exitValue) {
				Assert.assertTrue(true);
			}
		});
		
		ds.start(null);
		
		Thread.sleep(2000);
		
		ds.destroy();
		
		EasyMock.verify(abstractDeviceShellFactory);
		
		assertFalse(ds.isShellRunning());
		
	}
}
