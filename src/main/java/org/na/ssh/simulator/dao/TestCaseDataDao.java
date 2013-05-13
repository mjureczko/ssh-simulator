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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.na.ssh.simulator.exceptions.IncorrectTestCaseDataException;
import org.na.ssh.simulator.model.RequestEntryPojo;
import org.na.ssh.simulator.model.TestCaseDataPojo;
import org.na.ssh.simulator.model.XMLFileNodesNames;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @see TestCaseDataPojo dao
 * 
 * @author Patryk Chrusciel
 * 
 */
public class TestCaseDataDao {
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * 
	 * @throws FileNotFoundException
	 */
	public File getFileByName(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		return file;
	}
	
	/**
	 * Get data for testing
	 * 
	 * @param testCaseFile
	 * @return
	 * @throws IncorrectTestCaseDataException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public TestCaseDataPojo getByFile(File file) throws IncorrectTestCaseDataException {
		
		Document doc = prepareDocumentFromFile(file);
		
		TestCaseDataPojo testCaseData = new TestCaseDataPojo();
		
		testCaseData.setDeviceType(doc
				.getElementsByTagName(XMLFileNodesNames.DEVICE_TYPE_ATTRIBUTE).item(0)
				.getTextContent());
		testCaseData.setPassword(doc.getElementsByTagName(XMLFileNodesNames.PASSWORD_ATTRIBUTE)
				.item(0).getTextContent());
		testCaseData.setLogin(doc.getElementsByTagName(XMLFileNodesNames.LOGIN_ATTRIBUTE).item(0)
				.getTextContent());
		
		NodeList requestNodes = doc.getElementsByTagName(XMLFileNodesNames.REQUEST_NODE);
		List<RequestEntryPojo> requests = new ArrayList<RequestEntryPojo>();
		for (int i = 0; i < requestNodes.getLength(); i++) {
			getRequestDataFromNode(requests, requestNodes, i);
		}
		testCaseData.setRequests(requests);
		
		return testCaseData;
	}
	
	private Document prepareDocumentFromFile(File file) throws IncorrectTestCaseDataException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IncorrectTestCaseDataException(e);
		}
		Document doc;
		try {
			doc = docBuilder.parse(file);
		} catch (SAXException e) {
			throw new IncorrectTestCaseDataException(e);
		} catch (IOException e) {
			throw new IncorrectTestCaseDataException(e);
		}
		
		doc.getDocumentElement().normalize();
		
		return doc;
	}
	
	private void getRequestDataFromNode(List<RequestEntryPojo> requests, NodeList requestNodes,
			int i) {
		Node requestNode = requestNodes.item(i);
		Node delayInMsNode = requestNode.getAttributes().getNamedItem(
				XMLFileNodesNames.DELAY_IN_MS_ATTRIBUTE);
		
		Element eElement = (Element) requestNode;
		
		String command = eElement.getElementsByTagName(XMLFileNodesNames.REQUEST_COMMAND_ELEMENT)
				.item(0).getTextContent();
		String response = eElement.getElementsByTagName(XMLFileNodesNames.RESPONSE_MESSAGE_ELEMENT)
				.item(0).getTextContent();
		String prompt = eElement.getElementsByTagName(XMLFileNodesNames.RESPONSE_PROMPT_ELEMENT)
				.item(0).getTextContent();
		
		requests.add(new RequestEntryPojo(response, prompt, Long.parseLong(delayInMsNode
				.getTextContent()), command));
	}
}
