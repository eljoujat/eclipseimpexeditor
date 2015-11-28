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
package org.eclipseplugins.impexeditor.core.editor;


import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipseplugins.impexeditor.core.config.ImpexConfiguration;
import org.eclipseplugins.impexeditor.core.config.ImpexDataDefinition;


public class ImpexEditor extends AbstractDecoratedTextEditor
{

	private final ColorManager colorManager;
	private final ImpexDataDefinition impexDataDeffinition;
	StyleRange underLineRange = null;

	public ImpexEditor()
	{
		colorManager = new ColorManager();
		impexDataDeffinition = new ImpexDataDefinition();
		setSourceViewerConfiguration(new ImpexConfiguration(colorManager, impexDataDeffinition, getPreferenceStore()));
		setDocumentProvider(new ImpexDocumentProvider());
	}


	@Override
	protected void initializeEditor()
	{
		super.initializeEditor();
		configureInsertMode(SMART_INSERT, true);
		setInsertMode(INSERT);
	}

	@Override
	public void dispose()
	{
		colorManager.dispose();
		super.dispose();
	}

	public IDocument getDocument()
	{
		return getSourceViewer().getDocument();
	}


	public ISourceViewer getViewer()
	{
		return getSourceViewer();
	}

	/**
	 * @return the underLineRange
	 */
	public StyleRange getUnderLineRange()
	{
		return underLineRange;
	}

	/**
	 * @param underLineRange the underLineRange to set
	 */
	public void setUnderLineRange(final StyleRange underLineRange)
	{
		this.underLineRange = underLineRange;
	}


}
