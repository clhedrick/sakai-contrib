/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/util/html/SafeHtml.java $ 
 * $Id: SafeHtml.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011, 2012 Etudes, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 ***********************************************************************************/
package org.etudes.component.app.jforum.util.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SafeHtml
{
	private static final Log logger = LogFactory.getLog(SafeHtml.class);
	//private Set welcomeTags;

	private SafeHtml()
	{
	}

	/**
	 * Replace the characters < and > with &lt; and &gt; associated with text "script"
	 * 
	 * @param contents
	 *        string that is to be modified
	 * @return modifeed string
	 */
	public static String escapeJavascript(String contents)
	{
		if (contents == null) return null;

		String result = null;
		result = contents.replaceAll("<([\\/]*[sS][cC][rR][iI][pP][tT])>*?>", "&lt;$1&gt;");
		result = result.replaceAll("<([sS][cC][rR][iI][pP][tT])[\\s+][^\\<]*>*?>", "&lt;$1&gt;");

		return result;
	}

	/**
	 * Replace the characters < and > with space associated with text "script"
	 * 
	 * @param contents
	 *        string that is to be modified
	 * @return modifeed string
	 */
	public static String escapeScriptTagWithSpace(String contents)
	{
		if (contents == null) return null;

		String result = null;
		result = contents.replaceAll("<\\/*([sS][cC][rR][iI][pP][tT])>*?>", " $1 ");
		result = result.replaceAll("<([sS][cC][rR][iI][pP][tT])[\\s+][^\\<]*>*?>", " $1 ");

		return result;
	}

	/**
	 * regular expression that tests for the existence of malicious characters and replaces them with a space.
	 */
	public static String escapeWithSpace(String contents)
	{
		if (contents == null) return null;

		String filterPattern = "[<>{}\\[\\];\\&]";
		String result = contents.replaceAll(filterPattern, " ");

		return result;
	}

	/**
	 * remove excess spaces from the string
	 * 
	 * @param contents
	 *        post content
	 * @return modified content
	 */
	public static String removeExcessSpaces(String contents)
	{
		if (contents == null) return null;

		String filterPattern = "(&amp;nbsp;|&nbsp;){10,}+";
		String result = contents.replaceAll(filterPattern, " ");

		return result;
	}

	/**
	 * removes existing target attribute in the anchor tag and adds target="_blank"
	 * 
	 * @param contents
	 *        Post contest
	 * 
	 * @return Modified content with target="_blank" in anchor tags
	 */
	public static String addAnchorTarget(String contents)
	{
		if (contents == null)
		{
			return null;
		}

		StringBuffer sb = new StringBuffer();

		Pattern p = Pattern.compile("<(a)([^>]+)>", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher m = p.matcher(contents);

		while (m.find())
		{
			if (m.groupCount() == 2)
			{
				String group1 = m.group(1);
				String group2 = m.group(2);
				String modGroup2 = group2.replaceAll("(target\\s*=\\s*[\"\'][^\"\']*[\"\']\\s*)?", "");

				String modString = "<" + group1 + " target=\"_blank\" " + modGroup2 + ">";

				m.appendReplacement(sb, Matcher.quoteReplacement(modString));
			}
		}

		m.appendTail(sb);

		return sb.toString();
	}
}
