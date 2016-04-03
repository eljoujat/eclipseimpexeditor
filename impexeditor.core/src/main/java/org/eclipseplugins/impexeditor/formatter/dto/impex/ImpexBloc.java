package org.eclipseplugins.impexeditor.formatter.dto.impex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipseplugins.impexeditor.formatter.dto.AbstractBloc;

public class ImpexBloc extends AbstractBloc {

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
		Map<Integer, Integer> maxLengthPerColumn = getMaxLengthPerColumn();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lines.size(); i++) {
			ImpexLine lineEntry = lines.get(i);
			sb.append(lineEntry.format(maxLengthPerColumn));
			sb.append("\n");
		}
		return sb.toString();
	}

	Map<Integer, Integer> getMaxLengthPerColumn() {
		List<ImpexLine> lines = getLines();
		Map<Integer, Integer> maxLengthPerLine = new HashMap<>();
		for (ImpexLine impexLine : lines) {
			for (int i = 0; i < impexLine.getEntries().size(); i++) {
				int columnIndex = impexLine.isHeader() ? i : i + 1;
				int length = impexLine.getEntries().get(i).toString().length();
				Integer storedMax = maxLengthPerLine.get(columnIndex) != null ? maxLengthPerLine.get(columnIndex) : 0;
				Integer max = length > storedMax ? length : storedMax;
				maxLengthPerLine.put(columnIndex, max);
			}
		}

		return maxLengthPerLine;
	}

}
