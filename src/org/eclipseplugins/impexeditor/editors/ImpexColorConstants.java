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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

public abstract class  ImpexColorConstants {
	
	public static String TYPE_IMAGE_ID="hybris_type_image";
	public static String KEYWORD_IMAGE_ID="hybris_keyword_image";
	public static RGB XML_COMMENT = new RGB(128, 0, 0);
	public static RGB PROC_INSTR = new RGB(128, 128, 128);
	public static RGB STRING = new RGB(0, 128, 0);
	public static RGB DEFAULT = new RGB(0, 0, 0);
	public static RGB TAG = new RGB(0, 0, 128);
	public static RGB IMPEX_COMMAND = new RGB(34, 34, 255);
	public static RGB IMPEX_VARIABLE = new RGB(0, 191, 255);
	public static RGB IMPEX_HEADER_TYPE = new RGB(1, 223, 1);
	public static RGB IMPEX_MODIFIER = new RGB(255, 0, 255);
	public static RGB IMPEX_SEPARATOR = new RGB(254, 154, 46);
	public static RGB IMPEX_ATOMIC= new RGB(116,1,223);


	 public static final Map<String, String> IMPEX_KEYWORDS;
	static {
		Map<String, String> aMap = new HashMap<String, String>();
		aMap.put("INSERT", "impex_cmd");
		aMap.put("INSERT_UPDATE", "impex_cmd");
		aMap.put("REMOVE", "impex_cmd");
		aMap.put("UPDATE", "impex_cmd");
		aMap.put("alias", "impex_modifier");
		aMap.put("allownull", "impex_modifier");
		aMap.put("batchmode", "impex_modifier");
		aMap.put("cacheUnique", "impex_modifier");
		aMap.put("cellDecorator", "impex_modifier");
		aMap.put("collection-delimiter", "impex_modifier");
		aMap.put("dateformat", "impex_modifier");
		aMap.put("default", "impex_modifier");
		aMap.put("false", "impex_atomic");
		aMap.put("forceWrite", "impex_modifier");
		aMap.put("ignoreKeyCase", "impex_modifier");
		aMap.put("ignorenull", "impex_modifier");
		aMap.put("key2value-delimiter", "impex_modifier");
		aMap.put("lang", "impex_modifier");
		aMap.put("map-delimiter", "impex_modifier");
		aMap.put("mode", "impex_modifier");
		aMap.put("numberformat", "impex_modifier");
		aMap.put("parallel", "impex_modifier");
		aMap.put("path-delimiter", "impex_modifier");
		aMap.put("pos", "impex_modifier");
		aMap.put("processor", "impex_modifier");
		aMap.put("translator", "impex_modifier");
		aMap.put("true", "impex_atomic");
		aMap.put("unique", "impex_modifier");
		aMap.put("virtual", "impex_modifier");
		IMPEX_KEYWORDS = Collections.unmodifiableMap(aMap);
	}
}
