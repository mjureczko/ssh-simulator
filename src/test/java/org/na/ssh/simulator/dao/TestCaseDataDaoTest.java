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
package org.na.ssh.simulator.dao;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Test;
import org.na.ssh.simulator.exceptions.IncorrectTestCaseDataException;
import org.na.ssh.simulator.model.RequestEntryPojo;
import org.na.ssh.simulator.model.TestCaseDataPojo;

/**
 * 
 * @author Patryk Chrusciel
 * 
 */
public class TestCaseDataDaoTest {
	/**
	 * 
	 */
	@Test
	public void getFileByNameTest() {
		TestCaseDataDao dao = new TestCaseDataDao();
		try {
			File file = dao.getFileByName("src/test/resources/test_case_for_test.xml");
			assertEquals("test_case_for_test.xml", file.getName());
		} catch (FileNotFoundException e) {
			Assert.fail();
		}
	}
	
	/**
	 * @throws FileNotFoundException
	 * @throws IncorrectTestCaseDataException
	 * 
	 */
	@Test
	public void getByFileTest() throws FileNotFoundException, IncorrectTestCaseDataException {
		TestCaseDataDao dao = new TestCaseDataDao();
		File file = dao.getFileByName("src/test/resources/test_case_for_test.xml");
		
		TestCaseDataPojo testCaseData = dao.getByFile(file);
		
		assertEquals("CMTS", testCaseData.getDeviceType());
		assertEquals("password", testCaseData.getPassword());
		assertEquals("login", testCaseData.getLogin());
		
		RequestEntryPojo request1 = testCaseData.getRequests().get(0);
		assertEquals("command1", request1.getCommand());
		assertEquals("\nprompt1#", request1.getPrompt());
		assertEquals("responseMsg1", request1.getResponse());
		assertEquals(1, request1.getDelayInMs());
		
		RequestEntryPojo request2 = testCaseData.getRequests().get(1);
		assertEquals("command2", request2.getCommand());
		assertEquals("responseMsg2", request2.getResponse());
		assertEquals(2, request2.getDelayInMs());
		
	}
	
}
