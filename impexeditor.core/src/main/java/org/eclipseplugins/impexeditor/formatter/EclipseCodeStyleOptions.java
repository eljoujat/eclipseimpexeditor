package org.eclipseplugins.impexeditor.formatter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EclipseCodeStyleOptions {
//	public static final String lineLengthKey = "org.eclipse.jdt.core.formatter.lineSplit";
	public static final String lineLengthKey = DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT;
	public static final String commentLineLength = DefaultCodeFormatterConstants.FORMATTER_COMMENT_LINE_LENGTH; 
//	  "org.eclipse.jdt.core.formatter.comment.line_length";

	private static Map getCodeStyleEntries(File xmlFile)
			throws ParserConfigurationException, SAXException, IOException {
		Map entries = new HashMap();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("setting");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String id = eElement.getAttribute("id");
				String value = eElement.getAttribute("value");
				entries.put(id, value);
			}
		}
		return entries;
	}

	/**
	 * Return data structure compatible with @see JavaCore.getOptions
	 */
	public static Hashtable getCodeStyleSettingOptions(File codeStyleFile) {
		Map codeStyleOptions = null;
		try {
			codeStyleOptions = getCodeStyleEntries(codeStyleFile);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Hashtable options = JavaCore.getDefaultOptions();
		// Hashtable options = JavaCore.getOptions();

		options.putAll(codeStyleOptions);

		return options;
	}
}
