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
package mojo.dao.test;

import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import mojo.dao.core.DataService;
import mojo.dao.core.spec.ByKey;
import mojo.dao.core.spec.Select;
import mojo.dao.core.util.XHCollectionConverter;
import mojo.dao.core.util.XHProxyConverter;
import mojo.dao.test.model.Person;
import mojo.dao.test.model.Pet;

public class XStreamTest extends BaseTest {

	private DataService<Person> personService;
	private DataService<Pet> petService;
	private XStream xstream;

	private Person person;
	private Pet pet;

	public XStreamTest(String name) {
		super(name);

		personService = getBean("personService");
		petService = getBean("petService");

		xstream = new XStream();
		xstream.registerConverter(new XHProxyConverter());
		xstream.registerConverter(new XHCollectionConverter());
	}

	@Override
	protected void setUp() {
		super.setUp();

		person = personService.insert(new Person("Mickey Mouse"));
		pet = petService.insert(new Pet("Pluto", person));
	}

	@Override
	protected void tearDown() {
		petService.delete(pet.getId());
		personService.delete(person.getId());

		super.tearDown();
	}

	public void testProxy() {
		log("Testing uninitialized hibernate collection");
		Object tmp = personService.findById(person.getId());
		String xml = xstream.toXML(tmp);
		// System.out.println("XML: " + xml);
		assertEmptyElement(xml, "pets");
		assertEmptyElement(xml, "phones");

		log("Testing uninitialized hibernate proxy");
		tmp = petService.findById(pet.getId());
		xml = xstream.toXML(tmp);
		// System.out.println("XML: " + xml);
		assertEmptyElement(xml, "owner");

		log("Testing initialized hibernate proxy & collection");
		TransactionTemplate template = new TransactionTemplate(transactionManager);
		tmp = template.execute(new TransactionCallback<Object>() {

			public Object doInTransaction(TransactionStatus status) {
				Select<Object> query = new Select<Object>(Pet.class, new ByKey(pet.getId()));
				Pet pet = (Pet) repository.select(query).unique();
				pet.getOwner().getName(); // init proxy
				pet.getOwner().getPets().size(); // init collection
				return pet;
			}
		});

		xml = xstream.toXML(tmp);
		// System.out.println("XML: " + xml);
		assertNotEmptyElement(xml, "owner");
		assertNotEmptyElement(xml, "pets");
		assertEmptyElement(xml, "phones");
	}

	private void assertEmptyElement(String xml, String tag) {
		assertElement(xml, tag, true);
	}

	private void assertNotEmptyElement(String xml, String tag) {
		assertElement(xml, tag, false);
	}

	private void assertElement(String xml, String tag, boolean closed) {
		String regex = ".*<" + tag + "(\\s+\\w+=[\"'].*[\"']\\s*)?" + (closed ? "/" : "") + ">.*";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		// System.out.println("Pattern: " + pattern);
		assertTrue(pattern.matcher(xml).matches());
	}
}
