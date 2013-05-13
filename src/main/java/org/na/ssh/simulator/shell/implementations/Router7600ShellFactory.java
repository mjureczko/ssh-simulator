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
package org.na.ssh.simulator.shell.implementations;

import java.io.IOException;
import java.util.List;

import org.na.ssh.simulator.model.RequestEntryPojo;
import org.na.ssh.simulator.shell.AbstractDeviceShellFactory;

/**
 * Shell factory for CMTS
 * 
 * @author Patryk Chrusciel
 * 
 */
public class Router7600ShellFactory extends AbstractDeviceShellFactory {
	
	public Router7600ShellFactory(List<RequestEntryPojo> responses, String host, int report_port,
			int port, String testCaseFileName) throws IOException {
		super(responses, host, report_port, port, testCaseFileName);
	}
	
}
