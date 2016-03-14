package org.eclipseplugins.impexeditor.formatter.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImpexBloc {

	List<ImpexLine> lines;
	ImpexLine header;

	public ImpexLine getHeader() {
		return header;
	}

	public void setHeader(ImpexLine header) {
		this.header = header;
	}

	public List<ImpexLine> getLines() {
		return lines;
	}

	public void setLines(List<ImpexLine> lines) {
		this.lines = lines;
	}
	@Override
	public String toString() {
		Map<Integer, Integer> maxLengthPerColumn=getMaxLengthPerColumn();
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < lines.size(); i++) {
			ImpexLine lineEntry=lines.get(i);
			sb.append(lineEntry.format(maxLengthPerColumn));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	Map<Integer, Integer> getMaxLengthPerColumn(){
		List<ImpexLine> lines = getLines();
		Map<Integer, Integer> maxLengthPerLine = new HashMap<>();
		for (ImpexLine impexLine2 : lines) {
			for (int i = 0; i < impexLine2.getEntries().size(); i++) {
				int length = impexLine2.getEntries().get(i).toString().length();
				Integer storedMax=maxLengthPerLine.get(i)!=null?maxLengthPerLine.get(i):0;
				Integer max = length > storedMax ? length : storedMax;
				maxLengthPerLine.put(i, max);
			}
		}
		
		return maxLengthPerLine;
	}
	
	
	
}
