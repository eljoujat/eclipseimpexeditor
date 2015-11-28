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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipseplugins.impexeditor.core.config.ImpexDataDefinition;
import org.eclipseplugins.impexeditor.core.editor.ImpexScanner;

public class ImpexVariableRule implements IRule {

	IToken token;
    private final Pattern pattern=Pattern.compile("^\\$[a-zA-Z0-9_-]+");
    boolean spanNewLine = false;
    private ImpexDataDefinition impexDataDefinition;
    
    public ImpexVariableRule(IToken token ){
            this.token = token;     
    }
    
    public ImpexVariableRule(IToken token,ImpexDataDefinition imexDataDef ){
    this(token);
    this.impexDataDefinition=imexDataDef;
}
    public IToken evaluate(ICharacterScanner scanner) {
		return evaluateInternal((ImpexScanner) scanner);
	}
	
    
    public IToken evaluateInternal(ImpexScanner scanner) {

            String stream = "";
            int c = scanner.read();
            int count = 1;

            while( c != ICharacterScanner.EOF  ){
                                    
                    stream += (char) c;             
                    
                    Matcher m = pattern.matcher( stream );
                    if( m.matches()){
                    	c=scanner.read();
                    	if((']'== c || ')'==c || '='==c || '['==c || '('==c  || ';'==c ||c=='\n' ||c=='\r' )){
                    		scanner.unread();
                    		impexDataDefinition.addVariableDecalration(stream);
                    		scanner.setState("variable");
                    		return token;
                    	}else {
                    		scanner.unread();
                    	}
                            
                    }
                    
                    if( !spanNewLine && ( '\n' == c || '\r' == c ) ){
                            break;
                    }
                    
                    count++;
                    c = scanner.read();
            }

            //put the scanner back to the original position if no match
            for( int i = 0; i < count; i++){
                    scanner.unread();
            }
            
            return Token.UNDEFINED;
            
    }

}
