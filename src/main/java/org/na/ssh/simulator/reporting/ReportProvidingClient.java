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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.na.ssh.simulator.reporting.messages.MessageToClient;
import org.na.ssh.simulator.reporting.messages.MessageToServer;

/**
 * Client for report providing
 * 
 * @author Patryk Chrusciel
 * 
 */
public class ReportProvidingClient {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ReportProvidingClient.class);

	private Socket requestSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private MessageToClient msgToClient = new MessageToClient();
	private String host;
	private int port;

	public ReportProvidingClient(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
	}

	/**
	 * Send msg
	 * 
	 * @param cmdToServer
	 * @return
	 * @throws Exception
	 */
	public MessageToClient sendMsg(MessageToServer cmdToServer)
			throws Exception {
		try {
			requestSocket = new Socket(host, port);
			log.debug("Connected to " + host + " in port " + port);
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			return sendMessageAndGetResponse(cmdToServer);

		} finally {
			try {
				if (in != null) {
					in.close();
				} else {
					log.error("Unable to connect with ssh-simulator on " + host
							+ ". Check whether the ssh-simulator is running");
				}
				if (out != null) {
					out.close();
				}
				if (requestSocket != null) {
					requestSocket.close();
				}
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
			}
		}

	}

	/**
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private MessageToClient sendMessageAndGetResponse(
			MessageToServer msgToServer) throws IOException,
			ClassNotFoundException {
		sendMessage(msgToServer);
		msgToClient = (MessageToClient) in.readObject();
		log.debug("server> " + msgToClient);
		return msgToClient;
	}

	private void sendMessage(MessageToServer msg) {
		try {
			out.writeObject(msg);
			out.flush();
			log.debug("client> " + msg);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
	}

}