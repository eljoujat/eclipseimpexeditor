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
package org.eclipseplugins.impexeditor.editors;

import impexeditor.Activator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipseplugins.impexeditor.editors.config.ImpexDataDeffinition;
import org.eclipseplugins.impexeditor.editors.rules.ImpexHeaderRule;
import org.eclipseplugins.impexeditor.editors.rules.ImpexKeyWordRule;
import org.eclipseplugins.impexeditor.editors.rules.ImpexVariableRule;
import org.eclipseplugins.impexeditor.editors.rules.ImpexWhiteSpaceRule;
import org.eclipseplugins.impexeditor.editors.rules.ImpexWhitespaceDetector;
import org.eclipseplugins.impexeditor.preferences.PreferenceConstants;

import com.eclipsesource.json.JsonValue;


public class ImpexScanner extends RuleBasedScanner
{

	private String state;
	private String currentType;
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	public ImpexScanner(final ColorManager manager, final ImpexDataDeffinition impexDataDeffinition)
	{

		state = "INIT";
		final IRule[] rules = new IRule[6];
		// Add generic whitespace rule.
		rules[0] = new ImpexWhiteSpaceRule(new ImpexWhitespaceDetector());


		final Token default_text = new Token(new TextAttribute(
				manager.getColor(PreferenceConverter.getColor(store, PreferenceConstants.IMPEX_TEXT_COLOR)), null,
				SWT.NORMAL));
		//IMPEX CMD RULES
		final ImpexKeyWordRule impx_cmd_rule = new ImpexKeyWordRule(new IWordDetector()
		{
			@Override
			public boolean isWordStart(final char c)
			{
				return Character.isJavaIdentifierStart(c);
			}

			@Override
			public boolean isWordPart(final char c)
			{
				return Character.isJavaIdentifierPart(c);
			}
		});


		final ImpexKeyWordRule impex_modifierRule = new ImpexKeyWordRule(new IWordDetector()
		{
			@Override
			public boolean isWordStart(final char c)
			{
				return Character.isJavaIdentifierStart(c);
			}

			@Override
			public boolean isWordPart(final char c)
			{
				return Character.isJavaIdentifierPart(c);
			}
		});

		final ImpexKeyWordRule impexAtomicRule = new ImpexKeyWordRule(new IWordDetector()
		{
			@Override
			public boolean isWordStart(final char c)
			{
				return Character.isJavaIdentifierStart(c);
			}

			@Override
			public boolean isWordPart(final char c)
			{
				return Character.isJavaIdentifierPart(c);
			}
		});

		final ImpexHeaderRule headerTypeRule = new ImpexHeaderRule(new IWordDetector()
		{
			@Override
			public boolean isWordStart(final char c)
			{
				return Character.isJavaIdentifierStart(c);
			}

			@Override
			public boolean isWordPart(final char c)
			{
				return Character.isJavaIdentifierPart(c);
			}
		});

		final Token cmd_keyword = new Token(new TextAttribute(
				manager.getColor(PreferenceConverter.getColor(store, PreferenceConstants.IMPEX_COMMAND_COLOR)), null,
				SWT.BOLD));
		final Token impex_modifier = new Token(new TextAttribute(
				manager.getColor(PreferenceConverter.getColor(store, PreferenceConstants.IMPEX_MODIFIER_COLOR)), null,
				SWT.BOLD));
		final Token impex_atomic = new Token(new TextAttribute(
				manager.getColor(PreferenceConverter.getColor(store, PreferenceConstants.IMPEX_ATOMIC_COLOR)), null,
				SWT.BOLD));

		final Token impex_headerType = new Token(new TextAttribute(
				manager.getColor(PreferenceConverter.getColor(store, PreferenceConstants.IMPEX_HEADER_TYPE_COLOR)), null,
				SWT.BOLD));

		final Token impex_headerTypeAttribute = new Token(new TextAttribute(
				manager.getColor(PreferenceConverter.getColor(store, PreferenceConstants.IMPEX_HEADER_TYPE_COLOR))));


		for (final String impex_keyword : ImpexColorConstants.IMPEX_KEYWORDS.keySet())
		{
			if (ImpexColorConstants.IMPEX_KEYWORDS.get(impex_keyword) == "impex_cmd")
			{
				impx_cmd_rule.addWord(impex_keyword, cmd_keyword);

			}
			else if (ImpexColorConstants.IMPEX_KEYWORDS.get(impex_keyword) == "impex_modifier")
			{
				impex_modifierRule.addWord(impex_keyword, impex_modifier);

			}
			else if (ImpexColorConstants.IMPEX_KEYWORDS.get(impex_keyword) == "impex_atomic")
			{
				impexAtomicRule.addWord(impex_keyword, impex_atomic);
			}
		}

		for (final String headerTyp : impexDataDeffinition.getImpexDataDef().keySet())
		{
			headerTypeRule.addWord(headerTyp, impex_headerType);

			for (final JsonValue string : impexDataDeffinition.getImpexDataDef().get(headerTyp))
			{
				headerTypeRule.addWord(string.asString(), impex_headerTypeAttribute);
			}
		}

		rules[1] = new ImpexVariableRule(new Token(new TextAttribute(
				manager.getColor(PreferenceConverter.getColor(store, PreferenceConstants.IMPEX_VARIABLE_COLOR)), null,
				SWT.BOLD)), impexDataDeffinition);

		rules[2] = impx_cmd_rule;
		rules[3] = impex_modifierRule;
		rules[4] = impexAtomicRule;
		rules[5] = headerTypeRule;
		setDefaultReturnToken(default_text);
		setRules(rules);
	}

	public String getState()
	{
		return state;
	}

	public void setState(final String state)
	{
		this.state = state;
	}

	public String getCurrentType()
	{
		return currentType;
	}

	public void setCurrentType(final String currentType)
	{
		this.currentType = currentType;
	}


}
