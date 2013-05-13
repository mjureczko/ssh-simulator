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
package org.na.ssh.simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.Test;
import org.na.ssh.simulator.dao.TestCaseDataDao;
import org.na.ssh.simulator.exceptions.IncorrectTestCaseDataException;

public class MainTest extends Main {
	
	@Test
	public final void testMainValid() throws FileNotFoundException, IOException,
			IncorrectTestCaseDataException, InterruptedException {
		
		SshSimulator ssh = EasyMock.createMock(SshSimulator.class);
		
		TestCaseDataDao dao = new TestCaseDataDao();
		
		File file = dao.getFileByName("src/test/resources/test_case_for_test.xml");
		String[] args = { file.toString(), "-p", "5022", "-h", "127.0.0.1" };
		
		ssh.startSimulator(EasyMock.cmpEq(file), EasyMock.eq("127.0.0.1"), EasyMock.eq(5022),
				EasyMock.eq(2004));
		
		EasyMock.replay(ssh);
		
		Main main = new Main();
		main.ssh = ssh;
		
		main.execute(args);
		
		EasyMock.verify(ssh);
		
	}
	
}
