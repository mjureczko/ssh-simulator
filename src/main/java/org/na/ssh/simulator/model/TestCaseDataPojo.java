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
package org.na.ssh.simulator.model;

import java.util.List;

/**
 * Data for test case
 * 
 * @author Patryk Chrusciel
 * 
 */
public class TestCaseDataPojo {

	private String login;
	private String password;
	private String deviceType;
	private List<RequestEntryPojo> requests;

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param deviceType
	 *            the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * @param requests
	 *            the requests to set
	 */
	public void setRequests(List<RequestEntryPojo> requests) {
		this.requests = requests;
	}

	/**
	 * @return the requests
	 */
	public List<RequestEntryPojo> getRequests() {
		return requests;
	}
}
