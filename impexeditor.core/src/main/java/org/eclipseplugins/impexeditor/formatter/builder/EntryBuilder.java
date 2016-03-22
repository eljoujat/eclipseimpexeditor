package org.eclipseplugins.impexeditor.formatter.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipseplugins.impexeditor.core.config.ImpexDataDefinition;
import org.eclipseplugins.impexeditor.core.editor.ImpexColorConstants;
import org.eclipseplugins.impexeditor.formatter.dto.impex.EntryData;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class EntryBuilder {

	private final ImpexDataDefinition impexDataDefinition;

	public EntryBuilder(ImpexDataDefinition impexDataDefinition) {
		this.impexDataDefinition = impexDataDefinition;
	}

	public EntryData buildEntryData(String rawEntry,String impexType) {
		EntryData entryData = new EntryData();
		entryData.setEmpty(rawEntry == null || rawEntry.isEmpty());
		entryData.setValue(rawEntry.replaceAll("\\n+|\\r+", ""));
		entryData.setHeader(isHeaderColumn(rawEntry,impexType));
		return entryData;
	}

	public EntryData buildeFakeHeader(int headerLenght) {
		EntryData entry = new EntryData();
		entry.setHeader(false);
		entry.setValue(entry.appendNSpace(headerLenght + 2) + ";");
		return entry;
	}

	private boolean isHeaderColumn(String entry,String itemType) {

		final List<String> result = new ArrayList<String>();
		boolean isheader=false;

		for (final String keywords : ImpexColorConstants.IMPEX_KEYWORDS.keySet()) {
			if("impex_cmd".equalsIgnoreCase(ImpexColorConstants.IMPEX_KEYWORDS.get(keywords)) && entry.toUpperCase().startsWith(keywords) && entry.split(" ").length==2){
				return true;
			}
			if (keywords.toUpperCase().equals(entry.toUpperCase()) && !result.contains(keywords)) {
				return true;
			}
		}

		Map<String, JsonArray> nodeMap =
				new TreeMap<String, JsonArray>(String.CASE_INSENSITIVE_ORDER);
		nodeMap.putAll(impexDataDefinition.getImpexDataDef());
		for (final JsonValue string : nodeMap.get(itemType)) {
			if (entry.toUpperCase().contains(string.asString().toUpperCase())) {
				return true;
			}
		}

		if(entry.startsWith("$")) return true;

		for (final String var : impexDataDefinition.getDecalredVariables()) {
			if (var.toUpperCase().equals(entry.toUpperCase()) || entry.startsWith("$")) {
				return true;
			}
		}

		return isheader;

	}
}
