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
package org.eclipseplugins.impexeditor.core.editor.preferences;


import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipseplugins.impexeditor.core.Activator;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.P_HOST_ENDPOINT_STRING,
				"http://localhost:9001/hac");
		
		store.setDefault(PreferenceConstants.P_HOST_USER_NAME_STRING,
				"admin");
		
		store.setDefault(PreferenceConstants.P_HOST_PWD_STRING,
				"nimda");
		
		PreferenceConverter.setDefault(store, PreferenceConstants.IMPEX_TEXT_COLOR, new RGB(0, 0, 0));
		PreferenceConverter.setDefault(store, PreferenceConstants.IMPEX_COMMAND_COLOR, new RGB(34, 34, 255));
		PreferenceConverter.setDefault(store, PreferenceConstants.IMPEX_ATOMIC_COLOR, new RGB(116,1,223));
		PreferenceConverter.setDefault(store, PreferenceConstants.IMPEX_HEADER_TYPE_COLOR, new RGB(1, 223, 1));
		PreferenceConverter.setDefault(store, PreferenceConstants.IMPEX_MODIFIER_COLOR, new RGB(255, 0, 255));
		PreferenceConverter.setDefault(store, PreferenceConstants.IMPEX_VARIABLE_COLOR, new RGB(0, 191, 255));
		PreferenceConverter.setDefault(store, PreferenceConstants.IMPEX_COMMENTS_COLOR, new RGB(165, 42, 42));
		
		
		
		
	}

	
}
