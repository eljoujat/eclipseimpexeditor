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
package org.eclipseplugins.impexeditor.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import org.eclipse.core.internal.preferences.Base64;
import org.eclipseplugins.impexeditor.editors.config.ImpexDataDeffinition;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

@SuppressWarnings("restriction")
public class ImpexDataDef implements Serializable {

	private static final long serialVersionUID = 1426509408411130121L;

	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws Exception {
		String serializedObject = "";
		ImpexDataDeffinition impexDataDeffinition=new ImpexDataDeffinition();
		impexDataDeffinition.loadImpexDataDef();
		Map<String,JsonArray> myObject=impexDataDeffinition.getImpexDataDef();
		 // serialize the object
		 try {
		     ByteArrayOutputStream bo = new ByteArrayOutputStream();
		     ObjectOutputStream so = new ObjectOutputStream(bo);
		
			so.writeObject(myObject);
		     so.flush();
		     serializedObject = new String(Base64.encode(bo.toByteArray()));
		     
		 } catch (Exception e) {
		     System.out.println(e);
		     System.exit(1);
		 }
		 // deserialize the object
		 try {
			byte b[] = Base64.decode(serializedObject.getBytes()); 
		     ByteArrayInputStream bi = new ByteArrayInputStream(b);
		     ObjectInputStream si = new ObjectInputStream(bi);
		     Map<String,JsonArray> obj = (Map<String,JsonArray>) si.readObject();
		     for (String type : obj.keySet()) {
					System.out.println("type :"+type);
					 for (JsonValue string : obj.get(type)) {
						System.out.println("---- " +string.asString());
					}
				}
		 } catch (Exception e) {
		     System.out.println(e);
		     System.exit(1);
		 }
		
	}
}
