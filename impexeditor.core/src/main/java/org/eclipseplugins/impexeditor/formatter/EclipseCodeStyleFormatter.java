package org.eclipseplugins.impexeditor.formatter;

import java.io.File;
import java.util.Hashtable;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class EclipseCodeStyleFormatter {

	public static String dir = "./res/eclipse-code-style-files/";
	public static String FILE_VERTICALLY_LONG_STYLE = dir
			+ "vertically-long.xml";
	public static String FILE_ECLIPSE_BUILT_IN_STYLE = dir
			+ "eclipse-built-in.xml";
	public static String FILE_COMPACT_STYLE = dir + "compact.xml";

	public static String format(String code, File eclipseCodeStyleFile) {
		return format(code, eclipseCodeStyleFile, -1);
	}

	public static String format(String code, File eclipseCodeStyleFile,
			int lineLength) {
		IDocument document = new Document(code);
		Hashtable codeStyleOptions = EclipseCodeStyleOptions
				.getCodeStyleSettingOptions(eclipseCodeStyleFile);

		if (lineLength > 0) {
			codeStyleOptions.put(EclipseCodeStyleOptions.lineLengthKey,
					Integer.toString(lineLength));
			codeStyleOptions.put(EclipseCodeStyleOptions.commentLineLength,
					Integer.toString(lineLength));

			JavaCore.setOptions(codeStyleOptions);
		}

		CodeFormatter formatter = ToolFactory.createCodeFormatter(
				codeStyleOptions, ToolFactory.M_FORMAT_EXISTING);
    
		try {
			int indentationLevel = 0;
			TextEdit edit = formatter.format(CodeFormatter.K_UNKNOWN |
			    CodeFormatter.F_INCLUDE_COMMENTS, code, 0,
					code.length(), indentationLevel, null);
			if (edit != null)
				edit.apply(document);
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		String formattedCode = document.get();
		return formattedCode;
	}		
}
