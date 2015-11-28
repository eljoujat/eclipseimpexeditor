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
package org.eclipseplugins.impexeditor.core.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipseplugins.impexeditor.core.editor.ImpexScanner;

public class ImpexHeaderAttributeRule extends WordRule {
	private StringBuffer fheaderBuffer;
	public ImpexHeaderAttributeRule(IWordDetector detector) {
		super(detector);
		this.fheaderBuffer = new StringBuffer();
	}

	public IToken evaluate(ICharacterScanner scanner) {
		return evaluateInternal((ImpexScanner) scanner);
	}
	
	
	public IToken evaluateInternal(ImpexScanner scanner) {
		
		if(scanner.getState().equals("data")){
			return Token.UNDEFINED;
		}
		int c = scanner.read();
		if ( (c != -1)
				&& (this.fDetector.isWordStart((char) c))
				&& (((this.fColumn == -1) || (this.fColumn == scanner
						.getColumn() - 1)))) {
			this.fheaderBuffer.setLength(0);
			do {
				this.fheaderBuffer.append((char) c);
				c = scanner.read();
			} while ((c != -1) && (this.fDetector.isWordPart((char) c)));
			scanner.unread();
			
			String buffer = this.fheaderBuffer.toString();
			IToken token = (IToken) this.fWords.get(buffer);
			if (token != null) {
				c=scanner.read();
				if('['== c || ';'==c || '('==c || c==' ' || c=='\n' || c=='\r'){
					scanner.unread();
					scanner.setState("HEADER_ATTR");
					return token;
					
				}
				
			}
			if (this.fDefaultToken.isUndefined()) {
				unreadBuffer(scanner);
			}
			return Token.UNDEFINED;
		}

		scanner.unread();
		return Token.UNDEFINED;
	}
}
