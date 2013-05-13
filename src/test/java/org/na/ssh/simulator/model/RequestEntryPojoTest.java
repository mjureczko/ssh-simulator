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

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @see RequestEntryPojo
 * @author Patryk Chrusciel
 * 
 */
public class RequestEntryPojoTest {
	/**
	 * @see RequestEntryPojo#RequestEntryPojo(String, String)
	 */
	@Test
	public void promptConstructorTest() {
		RequestEntryPojo pojo = new RequestEntryPojo("response", "prompt#", "command1");
		assertEquals("\nprompt#", pojo.getPrompt());
		assertEquals("response", pojo.getResponse());
	}
	
	/**
	 * @see RequestEntryPojo#matches(String command)
	 */
	@Test
	public void isMatchingCommandTest() {
		RequestEntryPojo pojo = new RequestEntryPojo("response", "prompt#", "[A-Z]");
		assertTrue(pojo.matches("G"));
		assertFalse(pojo.matches("1"));
		assertFalse(pojo.matches("G3"));
		
		pojo = new RequestEntryPojo("response", "prompt#", "ls .*");
		assertTrue(pojo.matches("ls -a"));
		assertTrue(pojo.matches("ls -a fileName"));
		assertFalse(pojo.matches("dot ls -a"));
		
		pojo = new RequestEntryPojo("response", "prompt#", "[A-Z]");
		assertTrue(pojo.matches("G"));
		assertFalse(pojo.matches("1"));
		assertFalse(pojo.matches("G3"));
		
		pojo = new RequestEntryPojo("response", "prompt#", "command");
		assertTrue(pojo.matches("command"));
		pojo = new RequestEntryPojo("response", "prompt#", "command1");
		assertFalse(pojo.matches("command"));
		
	}
}
