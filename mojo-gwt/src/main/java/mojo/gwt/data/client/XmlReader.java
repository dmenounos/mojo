/*
 * Copyright (C) 2010 Dimitrios Menounos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mojo.gwt.data.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import mojo.gwt.data.client.convert.xml.XmlDataConverter;
import mojo.gwt.data.client.type.ClassType;

public class XmlReader<T> extends DataReader<T> {

	private String tag;

	public static <T> XmlReader<T> create(ClassType<T> type, String tag) {
		return new XmlReader<T>(type, tag);
	}

	public XmlReader(ClassType<T> type, String tag) {
		super(type);
		setTag(tag);
	}

	public String getTag() {
		return tag;
	}

	protected void setTag(String tag) {
		assert tag != null;
		this.tag = tag;
	}

	@Override
	public List<T> convert(Object data) {
		Document document = XMLParser.parse((String) data);
		NodeList nodes = document.getElementsByTagName(getTag());
		List<T> records = new ArrayList<T>(nodes.getLength());
		XmlDataConverter converter = new XmlDataConverter(document);

		for (int x = 0; x < nodes.getLength(); x++) {
			Element element = (Element) nodes.item(x);
			T record = converter.from(element, getType());
			records.add(record);
		}

		return records;
	}
}
