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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.na.ssh.simulator.exceptions.IncorrectRequestOrderException;
import org.na.ssh.simulator.exceptions.NonExistingCommandException;

/**
 * Shell factory for CNR
 * 
 * @author Patryk Chrusciel
 * 
 */
public class DeviceShell implements Command, Runnable {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
	.getLogger(DeviceShell.class);
	
	private AbstractDeviceShellFactory deviceShellFactory;
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;
	protected ExitCallback callback;
	protected Environment environment;
	protected Thread thread;
	private boolean isShellRunning;
	
	public DeviceShell(AbstractDeviceShellFactory abstractDeviceShellFactory) {
		deviceShellFactory = abstractDeviceShellFactory;
	}
	
	@Override
	public void start(Environment env) throws IOException {
		environment = env;
		// thread = new Thread(this, "EchoShell");
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		setShellRunning(true);
		StringBuffer sb = new StringBuffer();
		
		try {
			out.write("\n\r".getBytes());
			out.flush();
			out.write("\n\r $".getBytes());
			out.flush();
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
		
		try {
			while (isShellRunning()) {
				
				int b = r.read();
				
				// if is new line or carriage return char
				if (b == 13 || b == 10) {
					
					out.write("\n\r".getBytes());
					out.flush();
					
					String command = sb.toString();
					
					System.out.println("Command: " + command);
					
					if ("sshexit".equals(command)) {
						setShellRunning(false);
						break;
						
					}
					
					String response;
					
					if ("sshstatus".equals(command)) {
						
						response = deviceShellFactory.getServer().getStatusText();
						out.write(response.getBytes());
						if (!response.equals("") && !response.equals(" ")) {
							out.write("\n".getBytes());
						}
						out.write(("\r" + deviceShellFactory.getLastPrompt()).getBytes());
						out.flush();
						
					} else {
						response = createResponseForCommand(command);
						if (response != null) {
							out.write(response.getBytes());
							out.flush();
						}
					}
					sb = new StringBuffer();
				}
				
				else if (b != -1) {
					sb.append(Character.toChars(b));
					out.write(b);
					out.flush();
				}
				
			}
		} catch (IOException e) {
			log.error("Shell factory error: " + e.getLocalizedMessage());
		} finally {
			callback.onExit(0);
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
			}
		}
	}
	
	private String createResponseForCommand(String command) {
		String response = "";
		command = command.trim();
		log.info("Request command: '" + command + "'");
		try {
			response = deviceShellFactory.createResponse(command);
			long timeOut = deviceShellFactory.getDelayInMs(command);
			log.info("sleep for: " + timeOut + " milliseconds");
			try {
				Thread.sleep(timeOut);
			} catch (InterruptedException e) {
				log.warn(e.getMessage(), e);
			}
			log.info("Created response from device: '" + response + "'");
			return response;
		} catch (NonExistingCommandException e) {
			log.error("ERROR: " + response);
		} catch (IncorrectRequestOrderException e) {
			log.error("ERROR: " + response);
		}
		return null;
	}
	
	public InputStream getIn() {
		return in;
	}
	
	public OutputStream getOut() {
		return out;
	}
	
	public OutputStream getErr() {
		return err;
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
	/**
	 * Stop shell
	 */
	public void stopShell() {
		setShellRunning(false);
	}
	
	@Override
	public void setInputStream(InputStream in) {
		this.in = in;
	}
	
	@Override
	public void setOutputStream(OutputStream out) {
		this.out = out;
	}
	
	@Override
	public void setErrorStream(OutputStream err) {
		this.err = err;
	}
	
	@Override
	public void setExitCallback(ExitCallback callback) {
		this.callback = callback;
	}
	
	@Override
	public void destroy() {
		setShellRunning(false);
		thread.interrupt();
	}

	/**
	 * @param isShellRunning the isShellRunning to set
	 */
	public void setShellRunning(boolean isShellRunning) {
		this.isShellRunning = isShellRunning;
	}

	/**
	 * @return the isShellRunning
	 */
	public boolean isShellRunning() {
		return isShellRunning;
	}
}
