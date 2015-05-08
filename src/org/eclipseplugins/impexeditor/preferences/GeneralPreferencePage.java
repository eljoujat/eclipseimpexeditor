/*******************************************************************************
 * Copyright 2014 Youssef EL JAOUJAT.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.eclipseplugins.impexeditor.preferences;

import impexeditor.Activator;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class GeneralPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public GeneralPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Hybris Server Endpoint");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		
		addField(
				new StringFieldEditor(PreferenceConstants.P_HOST_ENDPOINT_STRING, "Host Name :", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PreferenceConstants.P_HOST_USER_NAME_STRING, "User Name :", getFieldEditorParent()));	
		addField(
				new StringFieldEditor(PreferenceConstants.P_HOST_PWD_STRING, "Password :", getFieldEditorParent()));	
		
		addField(
				new ColorFieldEditor(PreferenceConstants.IMPEX_TEXT_COLOR, "Main Text Content  :", getFieldEditorParent()));	
		
		addField(
				new ColorFieldEditor(PreferenceConstants.IMPEX_COMMAND_COLOR, "Command (INSERT,INSERT_UPDATE,..)  :", getFieldEditorParent()));	
		addField(
				new ColorFieldEditor(PreferenceConstants.IMPEX_ATOMIC_COLOR, "Atomic (true,false,mode,..)  :", getFieldEditorParent()));	
		addField(
				new ColorFieldEditor(PreferenceConstants.IMPEX_HEADER_TYPE_COLOR, "Header Type (Product, Promotion,..) :", getFieldEditorParent()));	
		addField(
				new ColorFieldEditor(PreferenceConstants.IMPEX_MODIFIER_COLOR, "Modifier (allownull,batchmode,..) :", getFieldEditorParent()));	
		addField(
				new ColorFieldEditor(PreferenceConstants.IMPEX_VARIABLE_COLOR, "Variable :", getFieldEditorParent()));	
		
	}

	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}