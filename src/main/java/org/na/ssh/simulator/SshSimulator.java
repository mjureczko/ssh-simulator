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
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.na.ssh.simulator.authentication.ConnectionAuthenticator;
import org.na.ssh.simulator.dao.TestCaseDataDao;
import org.na.ssh.simulator.exceptions.IncorrectTestCaseDataException;
import org.na.ssh.simulator.model.TestCaseDataPojo;
import org.na.ssh.simulator.reporting.TestCaseExecutionInfo;
import org.na.ssh.simulator.shell.DeviceSimulator;
import org.na.ssh.simulator.shell.implementations.ASRShellFactory;
import org.na.ssh.simulator.shell.implementations.CMTSShellFactory;
import org.na.ssh.simulator.shell.implementations.CNRShellFactory;
import org.na.ssh.simulator.shell.implementations.Router7600ShellFactory;

public class SshSimulator {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SshSimulator.class);

	private DeviceSimulator ds;

	public DeviceSimulator getDs() {
		return ds;
	}

	private String testCaseFileName;

	/**
	 * Starts SSH Simulator. Report port not used.
	 * 
	 * @param testCaseFile
	 *            File in XML format with Devinition of Test Case and Router
	 *            type @see test_case_shema.xsd
	 * @param host
	 *            IP of host (by default is used localhost)
	 * @param port
	 *            Port number to run the SSH
	 * 
	 * @throws IncorrectTestCaseDataException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void startSimulator(File testCaseFile, String host, int port)
			throws FileNotFoundException, IncorrectTestCaseDataException,
			IOException {
		this.startSimulator(testCaseFile, host, port, 0);
	}

	/**
	 * Starts SSH Simulator.
	 * 
	 * @param testCaseFile
	 *            File in XML format with Devinition of Test Case and Router
	 *            type @see test_case_shema.xsd
	 * @param host
	 *            IP of host (by default is used localhost)
	 * @param port
	 *            Port number to run the SSH
	 * @param report_port
	 *            Port number to run the reports
	 * 
	 * @throws IncorrectTestCaseDataException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void startSimulator(File testCaseFile, String host, int port,
			int report_port) throws IncorrectTestCaseDataException,
			IOException, FileNotFoundException {

		logPrepare(testCaseFile.getName());

		testCaseFileName = testCaseFile.getName();

		TestCaseDataDao dao = new TestCaseDataDao();

		File file = dao.getFileByName(testCaseFile.getPath());
		TestCaseDataPojo data = dao.getByFile(file);

		String deviceType = data.getDeviceType();

		ConnectionAuthenticator authenticator = new ConnectionAuthenticator(
				data.getLogin(), data.getPassword());

		if (host == null) {
			log.info("Setting default host (localhost)");
			host = "localhost";
		}
		if (port <= 0) {
			log.info("Setting default port (22)");
			port = 22;
		}

		if (report_port <= 0) {
			log.info("Report port is tunred off!");

		}

		log.info("Device type: " + deviceType);
		log.info("Test case file: " + testCaseFile);
		log.info("Host: " + host);
		log.info("Port: " + port);

		if (deviceType.equals("CMTS")) {
			ds = new DeviceSimulator(new CMTSShellFactory(data.getRequests(),
					host, report_port, port, testCaseFileName), authenticator,
					host, port);
		} else if (deviceType.equals("CNR")) {
			ds = new DeviceSimulator(new CNRShellFactory(data.getRequests(),
					host, report_port, port, testCaseFileName), authenticator,
					host, port);
		} else if (deviceType.equals("ASR")) {
			ds = new DeviceSimulator(new ASRShellFactory(data.getRequests(),
					host, report_port, port, testCaseFileName), authenticator,
					host, port);
		} else if (deviceType.equals("Router7600")) {
			ds = new DeviceSimulator(new Router7600ShellFactory(
					data.getRequests(), host, report_port, port,
					testCaseFileName), authenticator, host, port);
		} else {
			log.error("Given device type in test case is incorrect.");
			return;
		}

		start();

	}

	protected void start() throws IOException {
		ds.startSimulation();

	}

	/**
	 * Prepares logs to be written into specified subfolder in application run
	 * path
	 * 
	 * @param folderName
	 *            Name of subfolder
	 * @throws IOException
	 */
	protected void logPrepare(String folderName) throws IOException {

		String logFilePath = "./logs" + File.separatorChar + folderName;
		Date projDate = new Date(System.currentTimeMillis());
		StringBuffer dateStr = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		dateStr = sdf.format(projDate, dateStr, new FieldPosition(0));

		Logger rootLogger = Logger.getRootLogger();
		@SuppressWarnings("rawtypes")
		Enumeration appenders = rootLogger.getAllAppenders();

		while (appenders.hasMoreElements()) {
			Appender currAppender = (Appender) appenders.nextElement();
			FileAppender fa = null;
			if (currAppender instanceof FileAppender) {
				fa = (FileAppender) currAppender;

				if (!(new File(logFilePath)).mkdirs()
						&& !(new File(logFilePath)).exists())
					throw new FileNotFoundException(
							"Directory can not be read or write");
				else {

					String logFileName = logFilePath + File.separatorChar
							+ fa.getFile() + "_" + dateStr.toString() + ".log";
					fa.setFile(logFileName);
					fa.activateOptions();
					log.debug("\n**************Log file for this run: "
							+ logFileName + "\n**************\n");

				}
			}
		}
	}

	/**
	 * Used to stop simulation and release ports (ssh and reporting port)
	 */
	public void stopSimulation() {
		try {

			ds.stopSimulation();
			ds.getShell().getServer().setHaveToFinish();

		} catch (InterruptedException e) {

			log.error("Server stopped already", e);
		}

	}

	/**
	 * Rerutn Server Status directly
	 * 
	 * @return
	 */
	public TestCaseExecutionInfo getStatusServer() {
		return ds.getShell().getServer().getTestCaseStatusDirectly();
	}

	/**
	 * Reset Test Case
	 */
	public void resetTestCaseStatus() {
		ds.getShell().getServer().resetTestCase();

	}

	/**
	 * Is server running (based on report server)
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return ds.getShell().getServer().isRunning();
	}

	public String getTestCaseFileName() {

		return testCaseFileName;
	}

	public String getStatusServerText() {

		return ds.getShell().getServer().getStatusText();
	}

}
