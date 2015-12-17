/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/util/bbcode/BBCodeHandler.java $ 
 * $Id: BBCodeHandler.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.component.app.jforum.util.bbcode;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class BBCodeHandler extends DefaultHandler implements Serializable
{
	private static final long serialVersionUID = 3821317091827872555L;

	protected Map<String, BBCode> bbMap = new LinkedHashMap<String, BBCode>();
	protected Map<String, BBCode> alwaysProcessMap = new LinkedHashMap<String, BBCode>();
	protected boolean matchOpen = false;
	protected String tagName = "";
	protected StringBuffer sb;
	protected BBCode bb;

	public BBCodeHandler()
	{
	}

	public BBCodeHandler parse() throws Exception
	{
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		BBCodeHandler bbParser = new BBCodeHandler();

		String bbConfigPath = "bbcode/bb_config_plain.xml";

		InputStream in = null;
		try
		{
			in = this.getClass().getClassLoader().getResourceAsStream(bbConfigPath);
					
			InputSource input = new InputSource(in);
			parser.parse(input, bbParser);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (in != null)
			{
				in.close();
			}
		}	
		
		return bbParser;
	}

	public void addBb(BBCode bb)
	{
		if (bb.alwaysProcess())
		{
			this.alwaysProcessMap.put(bb.getTagName(), bb);
		}
		else
		{
			this.bbMap.put(bb.getTagName(), bb);
		}
	}

	public Collection<BBCode> getBbList()
	{
		return this.bbMap.values();
	}

	public Collection<BBCode> getAlwaysProcessList()
	{
		return this.alwaysProcessMap.values();
	}

	public BBCode findByName(String tagName)
	{
		return (BBCode) this.bbMap.get(tagName);
	}

	public void startElement(String uri, String localName, String tag, Attributes attrs)
	{
		if (tag.equals("match"))
		{
			this.matchOpen = true;
			this.sb = new StringBuffer();
			this.bb = new BBCode();

			String tagName = attrs.getValue("name");
			if (tagName != null)
			{
				this.bb.setTagName(tagName);
			}

			// Shall we remove the infamous quotes?
			String removeQuotes = attrs.getValue("removeQuotes");
			if (removeQuotes != null && removeQuotes.equals("true"))
			{
				this.bb.enableRemoveQuotes();
			}

			String alwaysProcess = attrs.getValue("alwaysProcess");
			if (alwaysProcess != null && "true".equals(alwaysProcess))
			{
				this.bb.enableAlwaysProcess();
			}
		}

		this.tagName = tag;
	}

	public void endElement(String uri, String localName, String tag)
	{
		if (tag.equals("match"))
		{
			this.matchOpen = false;
			this.addBb(this.bb);
		}
		else if (this.tagName.equals("replace"))
		{
			this.bb.setReplace(this.sb.toString().trim());
			this.sb.delete(0, this.sb.length());
		}
		else if (this.tagName.equals("regex"))
		{
			this.bb.setRegex(this.sb.toString().trim());
			this.sb.delete(0, this.sb.length());
		}

		this.tagName = "";
	}

	public void characters(char ch[], int start, int length)
	{
		if (this.tagName.equals("replace") || this.tagName.equals("regex")) this.sb.append(ch, start, length);
	}

	public void error(SAXParseException exception) throws SAXException
	{
		throw exception;
	}
}
