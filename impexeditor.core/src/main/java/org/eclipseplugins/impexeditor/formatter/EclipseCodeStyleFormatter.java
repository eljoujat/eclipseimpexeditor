package org.eclipseplugins.impexeditor.formatter;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipseplugins.impexeditor.core.config.ImpexDataDefinition;
import org.eclipseplugins.impexeditor.formatter.builder.EntryBuilder;
import org.eclipseplugins.impexeditor.formatter.builder.ImpexBlocBuilder;
import org.eclipseplugins.impexeditor.formatter.dto.AbstractBloc;
import org.eclipseplugins.impexeditor.formatter.dto.impex.EntryData;
import org.eclipseplugins.impexeditor.formatter.dto.impex.ImpexBloc;
import org.eclipseplugins.impexeditor.formatter.dto.variables.VariableBloc;

public class EclipseCodeStyleFormatter {

	public static String dir = "./res/eclipse-code-style-files/";
	public static String FILE_VERTICALLY_LONG_STYLE = dir + "vertically-long.xml";
	public static String FILE_ECLIPSE_BUILT_IN_STYLE = dir + "eclipse-built-in.xml";
	public static String FILE_COMPACT_STYLE = dir + "compact.xml";
	static public final String WITH_DELIMITER = "(?=%1$s)";

	public static String format(String code, File eclipseCodeStyleFile) {
		return format(code, eclipseCodeStyleFile, -1);
	}

	public static String format(String code, File eclipseCodeStyleFile, int lineLength) {
		IDocument document = new Document(code);
		Hashtable<Object, Object> codeStyleOptions = EclipseCodeStyleOptions
				.getCodeStyleSettingOptions(eclipseCodeStyleFile);

		if (lineLength > 0) {
			codeStyleOptions.put(EclipseCodeStyleOptions.lineLengthKey, Integer.toString(lineLength));
			codeStyleOptions.put(EclipseCodeStyleOptions.commentLineLength, Integer.toString(lineLength));
		}

		CodeFormatter formatter = ToolFactory.createCodeFormatter(codeStyleOptions, ToolFactory.M_FORMAT_EXISTING);

		try {
			int indentationLevel = 0;
			TextEdit edit = formatter.format(CodeFormatter.K_UNKNOWN | CodeFormatter.F_INCLUDE_COMMENTS, code, 0,
					code.length(), indentationLevel, null);
			if (edit != null) {
				edit.apply(document);
			}
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		String formattedCode = document.get();
		return formattedCode;
	}

	public String format(String content, ImpexDataDefinition impexDataDefinition) {

		Scanner scanner = new Scanner(content);
		List<AbstractBloc> impexBlocs = new ArrayList<>();
		StringBuilder sb=new StringBuilder();
		while (scanner.hasNext()) {
			String bloc = scanner.useDelimiter(
					String.format(WITH_DELIMITER, "(?m)^\\$.*$|(?m)^#.*$|INSERT_UPDATE|INSERT.*(?<!_)$|(?<!_)UPDATE|REMOVE"))
					.next();
			if(isImpexCmd(bloc)){
				String impexContent = extractImpexContent(bloc);
				String rawImpexBloc = impexContent.replaceAll("\r|\n", "");
				EntryBuilder entryBuilder = new EntryBuilder(impexDataDefinition);
				String[] entries = rawImpexBloc.split(";");
				LinkedList<EntryData> entriesData = new LinkedList<>();
				int nbrOfEntriePerLineTemp = 0;
				int nbrOfEntriePerLine = 0;
				String itemType = findItemType(entries, impexDataDefinition);
				for (String rawEntry : entries) {
					EntryData entryData = entryBuilder.buildEntryData(rawEntry, itemType);
					if (nbrOfEntriePerLine > 0) {
						entryData.setHeader(false);
					}
					if (entryData.isHeader()) {
						nbrOfEntriePerLineTemp = nbrOfEntriePerLineTemp + 1;
					} else {
						nbrOfEntriePerLine = nbrOfEntriePerLineTemp;
					}

					entriesData.add(entryData);

				}
				ImpexBlocBuilder blocBuilder = new ImpexBlocBuilder();
				int nbrOfLines = entriesData.size() / (nbrOfEntriePerLine - 1);
				ImpexBloc impexBloc = blocBuilder.buildImpexBloc(entriesData, nbrOfEntriePerLine - 1, nbrOfLines);
				sb.append("\n");
				sb.append(impexBloc.toString());
				impexBlocs.add(impexBloc);
			}else if(isVariableBloc(bloc)){
				sb.append(VariableBloc.buildVariableBloc(bloc));
			}else{
				sb.append(bloc);
			}

		}
		scanner.close();
		return sb.toString();
	}



	private boolean isVariableBloc(String bloc) {
		String findItemRegex = "(?m)^\\$.*$";
		Pattern pattern = Pattern.compile(findItemRegex);
		Matcher matcher = pattern.matcher(bloc.toUpperCase());
		return matcher.find();
	}

	private String extractImpexContent(final String content) {
		String contentwihtNocomments = content.replaceAll("#.*", "");
		return contentwihtNocomments.replaceAll("\n\\$.*", "");
	}

	boolean isImpexCmd(String entryValue) {
		String findItemRegex = "(INSERT|UPDATE|INSERT_UPDATE|REMOVE){1} [a-zA-Z0-9$_]+";
		Pattern pattern = Pattern.compile(findItemRegex);
		Matcher matcher = pattern.matcher(entryValue.toUpperCase());
		return matcher.find();
	}

	String findItemType(String[] entries, ImpexDataDefinition impexDataDefinition) {
		String findItemRegex = "(INSERT|UPDATE|INSERT_UPDATE|REMOVE){1} [a-zA-Z0-9$_]+";
		Pattern pattern = Pattern.compile(findItemRegex);
		for (int i = 0; i < entries.length; i++) {
			Matcher matcher = pattern.matcher(entries[i].toUpperCase());
			while (matcher.find()) {
				return matcher.group(0).split(" ")[1];
			}
		}

		return null;
	}

}
