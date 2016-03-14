package org.eclipseplugins.impexeditor.formatter.builder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipseplugins.impexeditor.formatter.dto.EntryData;
import org.eclipseplugins.impexeditor.formatter.dto.ImpexLine;

public class ImpexLineBuilder {

	public ImpexLine buildImpexLine(int nbrOfEntries, LinkedList<EntryData> entriesData) {
		Boolean isHeader = null;
		ImpexLine impexLine = new ImpexLine();
		List<EntryData> lineEntries = new ArrayList<>();
		for (int i = 0; i < nbrOfEntries-1; i++) {
			EntryData entry = entriesData.pollFirst();
			if(entry==null) continue;
			isHeader =isHeader==null? entry.isHeader():isHeader;
			lineEntries.add(entry);
		}
		impexLine.setEntries(lineEntries);
		impexLine.setHeader(isHeader);
		return impexLine;
	}




}
