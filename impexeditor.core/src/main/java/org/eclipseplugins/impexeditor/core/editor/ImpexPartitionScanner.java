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
package org.eclipseplugins.impexeditor.core.editor;

import org.eclipse.jface.text.rules.*;

public class ImpexPartitionScanner extends RuleBasedPartitionScanner {
	public final static String IMPEX_COMMENT = "__impex_comment";
	public final static String MACRO_DEFINITION = "__macro_def";

	public ImpexPartitionScanner() {

		IToken impexComment = new Token(IMPEX_COMMENT);
		IPredicateRule[] rules = new IPredicateRule[1];
		rules[0] = new SingleLineRule("#", "#", impexComment);
		setPredicateRules(rules);
	}
}
