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
package org.na.ssh.simulator.authentication;

import lombok.extern.log4j.Log4j;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

/**
 * Password authenticator.
 * 
 * @author Patryk Chrusciel
 * 
 */
@Log4j
public class ConnectionAuthenticator implements PasswordAuthenticator {
	
	private String login;
	private String password;
	
	public ConnectionAuthenticator(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	@Override
	public boolean authenticate(String arg0, String arg1, ServerSession arg2) {
		
		if (login.equals(arg0) && password.equals(arg1)) {
			log.info("User '" + arg0 + "' authenticated.");
			return true;
		}
		
		return false;
	}
}
