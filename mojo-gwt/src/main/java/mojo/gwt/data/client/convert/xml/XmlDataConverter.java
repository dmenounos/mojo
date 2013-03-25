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
package mojo.gwt.data.client.convert.xml;

import com.google.gwt.xml.client.Attr;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;

import mojo.gwt.data.client.convert.ScalarConverter;
import mojo.gwt.data.client.convert.ScalarConverterRegistry;
import mojo.gwt.data.client.type.ClassField;
import mojo.gwt.data.client.type.ClassType;
import mojo.gwt.data.client.type.ClassTypeRegistry;

public class XmlDataConverter {

	private Document document;

	public XmlDataConverter(Document document) {
		setDocument(document);
	}

	public Document getDocument() {
		return document;
	}

	protected void setDocument(Document document) {
		assert document != null;
		this.document = document;
	}

	public <T> T from(Element element, ClassType<T> type) {
		T record = type.create();

		for (ClassField field : type.getFields()) {
			String fieldName = field.getName();
			// get custom field mapping or resort to default (field name)
			String fieldMapping = (String) field.getParam("mapping", fieldName);

			// look for nodes or attributes with the field mapping
			NodeList nodes = element.getElementsByTagName(fieldMapping);
			Attr attr = element.getAttributeNode(fieldMapping);

			ScalarConverter<?> fieldConverter = getConverter(field);
			ClassType<?> fieldType = getType(field);

			if (fieldConverter != null && fieldType != null) {
				throw new IllegalStateException("Field '" + fieldName + "' cannot be both scalar and class.");
			}

			Object value = null;

			if (fieldConverter != null) {
				String str = null;

				// primitive types may be hosted
				// on either attribute or text node

				if (attr != null) {
					str = attr.getValue();
				}
				else if (nodes.getLength() == 1) {
					Element elem = (Element) nodes.item(0);
					Text text = (Text) elem.getFirstChild();
					str = text.getData();
				}

				// convert the extracted value into the correct type
				value = fieldConverter.parse(str);
			}
			else if (fieldType != null) {
				if (nodes.getLength() == 1) {
					Element subElement = (Element) nodes.item(0);
					value = from(subElement, fieldType);
				}
			}

			type.setProperty(record, fieldName, value);
		}

		return record;
	}

	public void to(Element element, Object obj) {
		// TODO
	}

	/**
	 * Returns a scalar converter for a given data field.
	 */
	protected ScalarConverter<?> getConverter(ClassField field) {
		// get custom field pattern or resort to default (null)
		String pattern = (String) field.getParam("pattern", null);
		return ScalarConverterRegistry.get(field.getKlass(), pattern);
	}

	/**
	 * Returns a class type for a given data field.
	 */
	protected ClassType<?> getType(ClassField field) {
		return ClassTypeRegistry.get(field.getKlass());
	}
}
