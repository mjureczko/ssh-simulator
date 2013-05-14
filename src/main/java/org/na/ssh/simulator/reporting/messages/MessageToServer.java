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
package org.na.ssh.simulator.reporting.messages;

import java.io.Serializable;

import org.na.ssh.simulator.reporting.commands.CommandToServer;

/**
 * Message to send to server from client
 * 
 * @author Patryk Chrusciel
 * 
 */

public class MessageToServer implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/** NoArgsConstructor */
	public MessageToServer() {

	}

	/** AllArgsConstructor */
	public MessageToServer(CommandToServer cmdToServer) {
		this.setCmdToServer(cmdToServer);
	}

	private CommandToServer cmdToServer;

	/**
	 * @param cmdToServer
	 *            the cmdToServer to set
	 */
	public void setCmdToServer(CommandToServer cmdToServer) {
		this.cmdToServer = cmdToServer;
	}

	/**
	 * @return the cmdToServer
	 */
	public CommandToServer getCmdToServer() {
		return cmdToServer;
	}
}
