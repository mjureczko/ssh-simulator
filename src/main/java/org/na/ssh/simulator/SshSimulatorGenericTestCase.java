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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.na.ssh.simulator.exceptions.IncorrectTestCaseDataException;
import org.na.ssh.simulator.reporting.TestCaseExecutionInfo;

public abstract class SshSimulatorGenericTestCase {
	public static final String XMLPATH = "src/test/resources/TestCasesXmls/";
	
	protected List<SshSimulator> sshSimulatorList = new ArrayList<SshSimulator>();
	
	/**
	 * Initialize and add new SshSimulator.
	 * 
	 * @param routerTestFileName
	 * @param routerAddress
	 * @param routerSshPort
	 */
	public void initializeNewSshServer(String deviceTestFileName, String deviceAddress,
			int deviceSshPort) throws InterruptedException, FileNotFoundException,
			IncorrectTestCaseDataException, IOException {
		// Starting instance of Ssh-simulator for test need
		File testCaseFile = new File(XMLPATH + deviceTestFileName + ".xml");
		
		SshSimulator sshSimulator = new SshSimulator();
		sshSimulator.startSimulator(testCaseFile, deviceAddress, deviceSshPort);
		sshSimulatorList.add(sshSimulator);
		
	}
	
	/**
	 * Is executed after test
	 * 
	 * @return
	 * @throws Exception
	 */
	@After
	public void checkFinalValidyOfTests() throws Exception {
		// Can be @After bcs it is run always - even if tests fail.
		for (int ssh = 0; ssh < sshSimulatorList.size(); ssh++) {
			// === Verify test case ================================
			boolean isTestValid = verifyIsTestCaseOnSshSimulatorSuccess(sshSimulatorList.get(ssh));
			
			assertTrue(sshSimulatorList.get(ssh).getStatusServerText(), isTestValid);
			sshSimulatorList.get(ssh).stopSimulation();
		}
		sshSimulatorList.clear();
	}
	
	/**
	 * Verification whether test case on ssh simulator has been executed with
	 * success
	 * 
	 * @param host
	 * @throws Exception
	 */
	protected boolean verifyIsTestCaseOnSshSimulatorSuccess(SshSimulator sshSimulator)
			throws Exception {
		TestCaseExecutionInfo status = sshSimulator.getStatusServer();
		return status.isTestCaseSuccess();
	}
	
}
