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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.na.ssh.simulator.model.RequestEntryPojo;

/**
 * @see RequestEntryControl
 * @author Patryk Chrusciel
 * 
 */
public class RequestEntryControlTest {
	
	private String command = "terminal length 1";
	private List<RequestEntryPojo> requests;
	private RequestEntryControl requestEntryControl = new RequestEntryControl();
	private List<RequestEntryPojo> regexRequests;
	
	/** */
	@Before
	public void before() {
		requests = new ArrayList<RequestEntryPojo>();
		requests.add(new RequestEntryPojo("resp", "p1", "terminal length 0"));
		requests.add(new RequestEntryPojo("resp", "p1", "terminal length 1"));
		requests.add(new RequestEntryPojo("resp", "p1", "terminal length 2"));
		
		regexRequests = new ArrayList<RequestEntryPojo>();
		regexRequests.add(new RequestEntryPojo("resp1", "p1", "terminal length.*"));
		regexRequests.add(new RequestEntryPojo("resp2", "p1", "termi[a-z]al length"));
	}
	
	/** */
	@After
	public void after() {
		requests = null;
		regexRequests = null;
	}
	
	/**
	 * @see RequestEntryControl#searchByCommand(List, String)
	 */
	@Test
	public void searchByCommandTest() {
		RequestEntryPojo request = requestEntryControl.searchByCommand(requests, command);
		assertEquals(command, request.getCommand());
		
		request = requestEntryControl.searchByCommand(requests, "terminal length 3");
		assertNull(request);
	}
	
	/**
	 * @see RequestEntryControl#searchByCommand(List, String)
	 */
	@Test
	public void searchByCommandRegexTest() {
		RequestEntryPojo request = requestEntryControl.searchByCommand(regexRequests, command);
		assertEquals("resp1", request.getResponse());
		request = requestEntryControl.searchByCommand(regexRequests, "termisal length");
		assertEquals("resp2", request.getResponse());
		request = requestEntryControl.searchByCommand(regexRequests, "termisal length 1");
		assertNull(request);
	}
}
