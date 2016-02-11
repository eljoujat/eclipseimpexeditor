/*******************************************************************************
 * Copyright 2014 Youssef EL JAOUJAT.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.eclipseplugins.impexeditor.core.editor.autocompletion;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipseplugins.impexeditor.core.Activator;
import org.eclipseplugins.impexeditor.core.config.ImpexDataDefinition;
import org.eclipseplugins.impexeditor.core.editor.ImpexColorConstants;
import org.eclipseplugins.impexeditor.core.editor.ImpexScanner;

import com.eclipsesource.json.JsonValue;


public class ImpexCompletionProposalComputer implements IContentAssistProcessor
{
	

	private static final ILog logger = Activator.getDefault().getLog();
	private static final ICompletionProposal[] NO_PROPOSALS = new ICompletionProposal[0];

	private final ImpexDataDefinition impexDataDefinition;
	private ImpexScanner impexScanner;

	public ImpexCompletionProposalComputer(
			final ImpexDataDefinition impexDataDeffinition, final ImpexScanner impexScanner)
	{
		this.impexDataDefinition = impexDataDeffinition;
		this.impexScanner = impexScanner;
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(final ITextViewer viewer,
			final int documentOffset)
	{

		String prefix = null;
		try
		{
			prefix = getPrefix(viewer, documentOffset);

			if (prefix == null || prefix.length() == 0)
			{
				return NO_PROPOSALS;
			}

		}
		catch (final BadLocationException e)
		{
			e.printStackTrace();
		}
		final IContextInformation info = new ContextInformation(prefix, prefix);
		return createProposals(prefix, documentOffset, info);
	}

	private CompletionProposal createProposal(final int documentOffset,
			final String prefix, final String keyword, final IContextInformation info, final Image image)
	{
		return new CompletionProposal(keyword, documentOffset
				- prefix.length(), prefix.length(), keyword.length(), image,
				keyword, info, keyword);
	}

	private String getPrefix(final ITextViewer viewer, int offset)
			throws BadLocationException
	{
		final IDocument doc = viewer.getDocument();
		if (doc == null || offset > doc.getLength())
		{
			return null;
		}

		int length = 0;
		while (--offset >= 0
				&& Character.isJavaIdentifierPart(doc.getChar(offset)))
		{
			length++;
		}

		return doc.get(offset + 1, length);
	}

	private CompletionProposal[] createProposals(final String searchPrefix, final int documentOffset,
			final IContextInformation info)
	{
		final List<String> result = new ArrayList<String>();

		final List<CompletionProposal> proposals = new ArrayList<CompletionProposal>();

		for (final String keywords : ImpexColorConstants.IMPEX_KEYWORDS.keySet())
		{
			if (keywords.toUpperCase().startsWith(searchPrefix.toUpperCase()) && !result.contains(keywords))
			{
				result.add(keywords);
				final Image image = Activator.getDefault().getImageRegistry().get(ImpexColorConstants.KEYWORD_IMAGE_ID);
				proposals.add(createProposal(documentOffset, searchPrefix, keywords, info, image));
			}
		}
		for (final String headerTyp : impexDataDefinition.getImpexDataDef().keySet())
		{
			if (headerTyp.toUpperCase().startsWith(searchPrefix.toUpperCase()) && !result.contains(headerTyp))
			{
				final Image image = Activator.getDefault().getImageRegistry().get(ImpexColorConstants.TYPE_IMAGE_ID);
				proposals.add(createProposal(documentOffset, searchPrefix, headerTyp, info, image));
				result.add(headerTyp);
			}
			for (final JsonValue string : impexDataDefinition.getImpexDataDef().get(
					headerTyp))
			{
				if (string.asString().toUpperCase().startsWith(searchPrefix.toUpperCase()) && !result.contains(string.asString()))
				{
					final Image image = Activator.getDefault().getImageRegistry().get(ImpexColorConstants.TYPE_IMAGE_ID);
					proposals.add(createProposal(documentOffset, searchPrefix, string.asString(), info, image));
					result.add(string.asString());
				}
			}

			for (final String var : impexDataDefinition.getDecalredVariables())
			{
				if (var.toUpperCase().startsWith(searchPrefix.toUpperCase()) && !result.contains(var))
				{
					final Image image = Activator.getDefault().getImageRegistry().get(ImpexColorConstants.TYPE_IMAGE_ID);
					proposals.add(createProposal(documentOffset, searchPrefix, var, info, image));
					result.add(var);
				}
			}
		}

		return proposals.toArray(new CompletionProposal[proposals.size()]);
	}

	@Override
	public IContextInformation[] computeContextInformation(final ITextViewer arg0,
			final int arg1)
	{
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ImpexScanner getImpexScanner()
	{
		return impexScanner;
	}

	public void setImpexScanner(final ImpexScanner impexScanner)
	{
		this.impexScanner = impexScanner;
	}

}
