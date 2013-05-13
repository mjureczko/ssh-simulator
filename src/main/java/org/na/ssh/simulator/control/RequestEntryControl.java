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
package org.na.ssh.simulator.control;

import java.util.List;

import org.na.ssh.simulator.model.RequestEntryPojo;

/**
 * Control for request entry
 * 
 * @author Patryk Chrusciel
 * 
 */
public class RequestEntryControl {
	/**
	 * Search request by command
	 * 
	 * @param requests
	 * @param command
	 * @return
	 */
	public RequestEntryPojo searchByCommand(List<RequestEntryPojo> requests, String command) {
		RequestEntryPojo requestEntryPojo;
		for (int i = 0; i < requests.size(); i++) {
			requestEntryPojo = requests.get(i);
			if (requestEntryPojo.matches(command)) {
				return requestEntryPojo;
			}
		}
		
		return null;
	}
}
