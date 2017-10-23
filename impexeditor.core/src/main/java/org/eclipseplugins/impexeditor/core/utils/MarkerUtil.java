package org.eclipseplugins.impexeditor.core.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipseplugins.impexeditor.core.dto.ImpexValidationDto;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public final class MarkerUtil {
	private static final int PMD_TAB_SIZE = 4;

	private MarkerUtil() {
	}


	public static void removeAllMarkers(IResource iresource) throws CoreException {
		iresource.deleteMarkers(IMarker.PROBLEM, true, 2);
	}

	public static IMarker addMarker(IResource iresource, ImpexValidationDto violation) throws CoreException {

		IMarker marker = iresource.createMarker(IMarker.PROBLEM);
		marker.setAttribute(IMarker.LINE_NUMBER, Math.max(violation.getBeginLine(), 0));
		 Range range = getAbsoluteRange(violation.getContent(), violation);
		    int start = Math.max(range.getStart(), 0);
		marker.setAttribute(IMarker.MESSAGE, violation.getValidationResultMsg());
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
	    int end = Math.max(range.getEnd(), 0);
	    marker.setAttribute(IMarker.CHAR_END, end);
		marker.setAttribute(IMarker.CHAR_START, start);
		return marker;
	}

	public static Range getAbsoluteRange(String content, ImpexValidationDto violation) {
		Range range;
		try {
			range = calculateAbsoluteRange(content, violation);
		} catch (BadLocationException e) {
			range = new Range(0, 0);
		}
		return range;
	}

	private static Range calculateAbsoluteRange(String content, ImpexValidationDto violation)
			throws BadLocationException {
		Document document = new Document(content);

		int start = getAbsolutePosition(content, document.getLineOffset(violation.getBeginLine() - 1),
				violation.getBeginColumn());
		int end = getAbsolutePosition(content, document.getLineOffset(violation.getEndLine() - 1),
				violation.getEndColumn());

		Range range;
		if (start <= end) {
			range = new Range(start - 1, end);
		} else {
			range = new Range(end - 1, start);
		}

		return range;
	}

	private static int getAbsolutePosition(String content, int lineOffset, int pmdCharOffset) {
		int pmdCharCounter = 0;
		int absoluteOffset = lineOffset;
		while (pmdCharCounter < pmdCharOffset) {
			if (absoluteOffset >= content.length())
				break;
			char c = content.charAt(absoluteOffset);
			if (c == '\t') {
				pmdCharCounter = (pmdCharCounter / PMD_TAB_SIZE + 1) * PMD_TAB_SIZE;
			} else {
				pmdCharCounter++;
			}

			absoluteOffset++;
		}
		return absoluteOffset;
	}

	public static final class Range {
		private final int start;
		private final int end;

		public Range(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}
	}

}
