package org.eclipseplugins.impexeditor.formatter.builder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipseplugins.impexeditor.formatter.dto.EntryData;
import org.eclipseplugins.impexeditor.formatter.dto.ImpexBloc;
import org.eclipseplugins.impexeditor.formatter.dto.ImpexLine;

public class ImpexBlocBuilder {

	private ImpexLineBuilder impexLineBuilder=new ImpexLineBuilder();

	public ImpexBloc buildImpexBloc(LinkedList<EntryData> entriesData,Integer nbrOfEntries, int nbrOfLines){
		ImpexBloc impexBloc=new ImpexBloc();
		ImpexLine header=null;
		List<ImpexLine> lines=new ArrayList<>();
		for (int i = 0; i < nbrOfLines; i++) {
			int nbrOfEntriesToTake=i==0?(nbrOfEntries+1):nbrOfEntries;
			ImpexLine impexLineEntry=impexLineBuilder.buildImpexLine(nbrOfEntriesToTake,entriesData);
			header=impexLineEntry.isHeader()?impexLineEntry:header;
			lines.add(impexLineEntry);
		}
		impexBloc.setHeader(header);
		impexBloc.setLines(lines);

		return impexBloc;
	}



}
