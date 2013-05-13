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
package org.na.ssh.simulator.reporting.commands;

/**
 * Commands for server
 * 
 * @author Patryk Chrusciel
 * 
 */
public enum CommandToServer {
	
	/**
	 * Get status about last executed test case by ssh-simulator
	 */
	WAS_TEST_CASE_SUCCESS,
	
	/**
	 * Reset test case status
	 */
	RESET_TEST_CASE_STATUS
	
}
