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
package org.eclipseplugins.impexeditor.actions;

import impexeditor.Activator;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipseplugins.impexeditor.http.ImpexHttpClient;
import org.eclipseplugins.impexeditor.preferences.PreferenceConstants;


/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class ValidateImpexAction implements IWorkbenchWindowActionDelegate
{
	private IWorkbenchWindow window;
	private final ILog logger = Activator.getDefault().getLog();

	private final ImpexHttpClient impexClient;

	/**
	 * The constructor.
	 */
	public ValidateImpexAction()
	{

		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		final String hostName = store.getDefaultString(PreferenceConstants.P_HOST_ENDPOINT_STRING);
		this.impexClient = new ImpexHttpClient(hostName);
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	@Override
	public void run(final IAction action)
	{

		final IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (!(part instanceof AbstractTextEditor))
		{
			return;
		}
		final ITextEditor editor = (ITextEditor) part;
		final IDocumentProvider dp = editor.getDocumentProvider();
		final IDocument doc = dp.getDocument(editor.getEditorInput());
		try
		{
			impexClient.validateImpex(doc.get(0, doc.getLength()));
		}
		catch (final BadLocationException e)
		{

			logger.log(new Status(Status.ERROR, Activator.PLUGIN_ID, Status.ERROR, "Bad Location Exception", e));
		}
	}

	/**
	 * Selection in the workbench has been changed. We
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	@Override
	public void selectionChanged(final IAction action, final ISelection selection)
	{
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	@Override
	public void dispose()
	{
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	@Override
	public void init(final IWorkbenchWindow window)
	{
		this.window = window;
	}
}
