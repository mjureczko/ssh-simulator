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

/**
 * Node names in XML files for test cases
 * 
 * @author Patryk Chrusciel
 * 
 */
public interface XMLFileNodesNames {
	/** */
	String ROOT_ELEMENT = "test_case";
	/** */
	String REQUEST_NODE = "request";
	/** */
	String DELAY_IN_MS_ATTRIBUTE = "delay_in_ms";
	/** */
	String REQUEST_COMMAND_ELEMENT = "request_command";
	/** */
	String RESPONSE_MESSAGE_ELEMENT = "response_message";
	/** */
	String RESPONSE_PROMPT_ELEMENT = "response_prompt";
	/** */
	String PASSWORD_ATTRIBUTE = "password";
	/** */
	String LOGIN_ATTRIBUTE = "login";
	/** */
	String DEVICE_TYPE_ATTRIBUTE = "device_type";
}
