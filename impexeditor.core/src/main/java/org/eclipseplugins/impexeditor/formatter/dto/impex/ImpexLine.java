package org.eclipseplugins.impexeditor.formatter.dto.impex;

import java.util.List;
import java.util.Map;

public class ImpexLine {

	boolean isHeader;
	List<EntryData> entries;

	int lineNubmer;


	public List<EntryData> getEntries() {
		return entries;
	}

	public void setEntries(List<EntryData> entries) {
		this.entries = entries;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}


	public String format(Map<Integer, Integer> maxLengthPerColumn) {
		StringBuffer sb=new StringBuffer();
		if(!isHeader()){
			sb.append(generateNSpace(maxLengthPerColumn.get(0)+2)+";");
		}
		for (int i = 0; i < entries.size(); i++) {
			EntryData entryData=entries.get(i);
			int columnIndex=isHeader()?i:i+1;
			int maxLenght=maxLengthPerColumn.get(columnIndex);
			String dataEntryString = entryData.appendNSpace(maxLenght+2);
			sb.append(dataEntryString);
			//sb.append(generateNSpace(maxLenght-dataEntryString.length()+2));
		}

		return sb.toString();
	}



	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();
		for (EntryData entryData : entries) {
			String dataEntryString = entryData.toString();
			sb.append(dataEntryString);
		}
		return sb.toString();
	}
	public String generateNSpace(int n){
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < n; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

}
