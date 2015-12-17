/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/util/post/PostUtil.java $ 
 * $Id: PostUtil.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011 Etudes, Inc. 
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
package org.etudes.component.app.jforum.util.post;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.etudes.api.app.jforum.Post;
import org.etudes.component.app.jforum.util.bbcode.BBCode;
import org.etudes.component.app.jforum.util.bbcode.BBCodeRepository;
import org.etudes.component.app.jforum.util.html.SafeHtml;

public class PostUtil
{

	public static Post preparePostForDisplay(Post p)
	{
		if (p.getText() == null)
		{
			return p;
		}
		
		/*if (p.isHtmlEnabled())
		{
			p.setText(p.getText().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		}*/
		
		p.setText(alwaysProcess(p.getText(), BBCodeRepository.getBBCollection().getAlwaysProcessList()));
		
		if (p.isBbCodeEnabled())
		{
			p.setText(processText(p.getText()));
		}
		
		// hyperlinks - add target
		//p.setText(SafeHtml.addAnchorTarget(p.getText()));

		return p;
	}
	
	public static String processText(String text)
	{
		if (text == null)
		{
			return null;
		}

		if (text.indexOf('[') > -1 && text.indexOf(']') > -1)
		{
			int openQuotes = 0;
			Iterator<BBCode> tmpIter = BBCodeRepository.getBBCollection().getBbList().iterator();

			while (tmpIter.hasNext())
			{
				BBCode bb = tmpIter.next();

				// Another hack for the quotes
				if (bb.getTagName().equals("openQuote") || bb.getTagName().equals("openSimpleQuote"))
				{
					Matcher matcher = Pattern.compile(bb.getRegex()).matcher(text);

					while (matcher.find())
					{
						openQuotes++;

						text = text.replaceFirst(bb.getRegex(), bb.getReplace());
					}
				}
				else if (bb.getTagName().equals("closeQuote"))
				{
					if (openQuotes > 0)
					{
						Matcher matcher = Pattern.compile(bb.getRegex()).matcher(text);

						while (matcher.find() && openQuotes-- > 0)
						{
							text = text.replaceFirst(bb.getRegex(), bb.getReplace());
						}
					}
				}
				else if (bb.getTagName().equals("code"))
				{
					Matcher matcher = Pattern.compile(bb.getRegex()).matcher(text);
					StringBuffer sb = new StringBuffer(text);

					while (matcher.find())
					{
						String contents = matcher.group(1);

						// Firefox seems to interpret <br> inside <pre>,
						// so we need this bizarre workaround
						contents = contents.replaceAll("<br>", "\n");

						// Do not allow other bb tags inside "code"
						contents = contents.replaceAll("\\[", "&#91;").replaceAll("\\]", "&#93;");

						// Try to bypass smilies interpretation
						contents = contents.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");

						// XML-like tags
						contents = contents.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

						StringBuffer replace = new StringBuffer(bb.getReplace());
						int index = replace.indexOf("$1");
						if (index > -1)
						{
							replace.replace(index, index + 2, contents);
						}

						index = sb.indexOf("[code]");
						int lastIndex = sb.indexOf("[/code]") + "[/code]".length();

						if (lastIndex > index)
						{
							sb.replace(index, lastIndex, replace.toString());
							text = sb.toString();
						}
					}
				}
				else
				{
					text = text.replaceAll(bb.getRegex(), bb.getReplace());
				}
			}

			if (openQuotes > 0)
			{
				BBCode closeQuote = BBCodeRepository.findByName("closeQuote");

				for (int i = 0; i < openQuotes; i++)
				{
					text = text + closeQuote.getReplace();
				}
			}
		}

		return text;
	}
	
	protected static String alwaysProcess(String text, Collection<BBCode> bbList)
	{
		if ((text == null) || (bbList == null))
		{
			return text;
		}
		
		for (Iterator<BBCode> iter = bbList.iterator(); iter.hasNext();)
		{
			BBCode bb = iter.next();
			text = text.replaceAll(bb.getRegex(), bb.getReplace());
		}

		return text;
	}
	
	private PostUtil(){}
}
