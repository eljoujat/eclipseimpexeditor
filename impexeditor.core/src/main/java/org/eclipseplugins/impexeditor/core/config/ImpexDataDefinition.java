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
package org.eclipseplugins.impexeditor.core.config;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.internal.preferences.Base64;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipseplugins.impexeditor.core.Activator;
import org.eclipseplugins.impexeditor.core.editor.preferences.PreferenceConstants;
import org.eclipseplugins.impexeditor.core.utils.ImpexHttpClient;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

@SuppressWarnings("restriction")
public class ImpexDataDefinition {
	private ILog logger=Activator.getDefault().getLog();
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	String hostName=store.getString(PreferenceConstants.P_HOST_ENDPOINT_STRING);
	private Set<String> decalredVariables = new HashSet<String>();
	private Map<String, JsonArray> impexDataDef = null;
	private Preferences preferences = InstanceScope.INSTANCE
			.getNode(Activator.PLUGIN_ID);
	private ImpexHttpClient impexHttpClient = new ImpexHttpClient(hostName);

	public Map<String, JsonArray> loadImpexDataDef() {

		impexDataDef = new HashMap<String, JsonArray>();
		try {
			for (JsonValue type : impexHttpClient.getAllTypes()) {
				impexDataDef.put(type.asString(), impexHttpClient
						.getTypeandAttribute(type.asString()).get("attributes")
						.asArray());
			}
			preferences.put(PreferenceConstants.P_IMPEX_DATA_DEF,
					serializeDateDef());
		} catch (Exception e) {
			
			logger.log(new Status(Status.ERROR, Activator.PLUGIN_ID, Status.ERROR, "Exception while attemting to refresch data def", e));
		}
		try {
			// prefs are automatically flushed during a plugin's "super.stop()".
			preferences.flush();
			
		} catch (BackingStoreException e) {
			logger.log(new Status(Status.ERROR, Activator.PLUGIN_ID, Status.ERROR, "BackingStoreException while attemting to refresch data def", e));
		}
		return impexDataDef;
	}

	public Map<String, JsonArray> getImpexDataDef() {
		String storedImpexDef = preferences.get(
				PreferenceConstants.P_IMPEX_DATA_DEF, "");
		if (impexDataDef != null && !impexDataDef.isEmpty())
			return impexDataDef;
		if (storedImpexDef==null || storedImpexDef.isEmpty()) {
			return loadImpexDataDef();
		} else {
			return this.impexDataDef = deserialiseImpexDataDef(storedImpexDef);
		}
	}

	private String serializeDateDef() {
		// serialize the object
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);

			so.writeObject(impexDataDef);
			so.flush();
			return new String(Base64.encode(bo.toByteArray()));

		} catch (Exception e) {
			logger.log(new Status(Status.ERROR, Activator.PLUGIN_ID, Status.ERROR, "BackingStoreException while attemting to serialise data def", e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, JsonArray> deserialiseImpexDataDef(
			String serializedObject) {
		Map<String, JsonArray> impexDatadef = null;
		// deserialize the object
		try {
			byte b[] = Base64.decode(serializedObject.getBytes());
			ByteArrayInputStream bi = new ByteArrayInputStream(b);
			ObjectInputStream si = new ObjectInputStream(bi);
			impexDatadef = (Map<String, JsonArray>) si.readObject();
		} catch (Exception e) {
			logger.log(new Status(Status.ERROR, Activator.PLUGIN_ID, Status.ERROR, "BackingStoreException while attemting to deserialise data def", e));
		}
		return impexDatadef;
	}

	public void addVariableDecalration(String varDec) {
		this.decalredVariables.add(varDec);
	}

	public Set<String> getDecalredVariables() {
		return decalredVariables;
	}

	public void setDecalredVariables(Set<String> decalredVariables) {
		this.decalredVariables = decalredVariables;
	}

}
