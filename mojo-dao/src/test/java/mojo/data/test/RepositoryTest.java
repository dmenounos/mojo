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

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import mojo.dao.core.DataPage;
import mojo.dao.core.Repository;
import mojo.dao.core.spec.ByKey;
import mojo.dao.core.spec.ByText;
import mojo.dao.core.spec.Insert;
import mojo.dao.core.spec.InsertDetail;
import mojo.dao.core.spec.Select;
import mojo.dao.core.spec.SelectDetail;
import mojo.dao.core.spec.Update;
import mojo.dao.core.spec.UpdateDetail;
import mojo.dao.test.model.Person;
import mojo.dao.test.model.Pet;
import mojo.dao.test.model.Phone;

public class RepositoryTest extends BaseTest {

	private Repository<Person> personRepository;
	private Repository<Pet> petRepository;
	private Repository<Phone> phoneRepository;

	public RepositoryTest(String name) {
		super(name);

		personRepository = getBean("personRepository");
		petRepository = getBean("petRepository");
		phoneRepository = getBean("phoneRepository");
	}

	public void testCRUD() {
		TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallback<Object>() {

			public Object doInTransaction(TransactionStatus status) {
				status.setRollbackOnly();
				String tmp = null;

				// simple typed repository
				// entity class has been set on the repository instance

				log("Creating Person A");
				Person personA = new Person(tmp = "Person A");
				Insert<Person> insertPerson = new Insert<Person>(personA);
				personA = personRepository.insert(insertPerson);
				assertValidPerson(personA, tmp);

				log("Retrieving Person A #" + personA.getId());
				Select<Person> selectPerson = new Select<Person>(new ByKey(personA.getId()));
				personA = personRepository.select(selectPerson).unique();
				assertValidPerson(personA, tmp);

				log("Modifying Person A #" + personA.getId());
				personA.setName(tmp = "Mickey Mouse");
				Update<Person> updatePerson = new Update<Person>(personA);
				personA = personRepository.update(updatePerson);
				assertValidPerson(personA, tmp);

				log("Retrieving Person A #" + personA.getId());
				personA = personRepository.select(selectPerson).unique();
				assertValidPerson(personA, tmp);

				// simple untyped repository
				// entity class is being set on each of the statement instances

				log("Creating Person B");
				Person personB = new Person(tmp = "Person B");
				Insert<Object> insertObject = new Insert<Object>(Person.class, personB);
				personB = (Person) repository.insert(insertObject);
				assertValidPerson(personB, tmp);

				log("Retrieving Person B #" + personB.getId());
				Select<Object> selectObject = new Select<Object>(Person.class, new ByKey(personB.getId()));
				personB = (Person) repository.select(selectObject).unique();
				assertValidPerson(personB, tmp);

				log("Modifying Person B #" + personB.getId());
				personB.setName(tmp = "Jon Arbuckle");
				Update<Object> updateObject = new Update<Object>(Person.class, personB);
				personB = (Person) repository.update(updateObject);
				assertValidPerson(personB, tmp);

				log("Retrieving Person B #" + personB.getId());
				personB = (Person) repository.select(selectObject).unique();
				assertValidPerson(personB, tmp);

				// many-to-one repository

				log("Creating Pet A");
				Pet petA = new Pet(tmp = "Pet");
				petA = petRepository.insert(new InsertDetail<Pet>(personB.getId(), petA));
				assertValidPet(petA, tmp, personB);

				log("Modifying Pet A");
				petA.setName(tmp = "Pluto");
				petA = petRepository.update(new UpdateDetail<Pet>(personA.getId(), petA));
				assertValidPet(petA, tmp, personA);

				log("Creating Pet B");
				Pet petB = new Pet(tmp = "Garfield");
				petB = petRepository.insert(new InsertDetail<Pet>(personB.getId(), petB));
				assertValidPet(petB, tmp, personB);

				log("Creating Pet C");
				Pet petC = new Pet(tmp = "Odie");
				petC = petRepository.insert(new InsertDetail<Pet>(personB.getId(), petC));
				assertValidPet(petC, tmp, personB);

				// one-to-many repository

				log("Creating Phone 1");
				Phone phoneA = new Phone(tmp = "9876543210");
				phoneA = phoneRepository.insert(new InsertDetail<Phone>(personB.getId(), phoneA));
				assertValidPhone(phoneA, tmp);

				log("Modifying Phone 1");
				phoneA.setNumber(tmp = "0123456789");
				phoneA = phoneRepository.update(new UpdateDetail<Phone>(personA.getId(), phoneA));
				assertValidPhone(phoneA, tmp);

				log("Creating Phone 2");
				Phone phoneB = new Phone(tmp = "0011223344");
				phoneB = phoneRepository.insert(new InsertDetail<Phone>(personB.getId(), phoneB));
				assertValidPhone(phoneB, tmp);

				log("Creating Phone 3");
				Phone phoneC = new Phone(tmp = "5566778899");
				phoneC = phoneRepository.insert(new InsertDetail<Phone>(personB.getId(), phoneC));
				assertValidPhone(phoneC, tmp);

				// plain selects

				log("Retrieving all persons");
				DataPage<?> page = repository.select(new Select<Object>(Person.class));
				assertValidDataPage(page, 2);

				log("Retrieving persons by property");
				page = repository.select(new Select<Object>(Person.class, new ByText("buck", "name")));
				assertValidDataPage(page, 1);

				// many-to-one selects

				log("Retrieving all pets");
				page = petRepository.select(new SelectDetail<Pet>());
				assertValidDataPage(page, 3);

				log("Retrieving pets by property");
				page = petRepository.select(new SelectDetail<Pet>(null, new ByText("lut", "name")));
				assertValidDataPage(page, 1);

				log("Retrieving PersonA's pets by property");
				page = petRepository.select(new SelectDetail<Pet>(personA.getId(), new ByText("arfiel", "name")));
				assertValidDataPage(page, 0);

				log("Retrieving PersonB's pets by property");
				page = petRepository.select(new SelectDetail<Pet>(personB.getId(), new ByText("arfiel", "name")));
				assertValidDataPage(page, 1);

				log("Retrieving PersonB's pets with pagging");
				page = petRepository.select(new SelectDetail<Pet>(personB.getId()).page(0, 1));
				assertValidDataPage(page, 1, 2);

				// one-to-many selects

				log("Retrieving all phones");
				page = phoneRepository.select(new SelectDetail<Phone>());
				assertValidDataPage(page, 3);

				log("Retrieving phones by property");
				page = phoneRepository.select(new SelectDetail<Phone>(null, new ByText("1234", "number")));
				assertValidDataPage(page, 1);

				log("Retrieving PersonA's phones by property");
				page = phoneRepository.select(new SelectDetail<Phone>(personA.getId(), new ByText("1122", "number")));
				assertValidDataPage(page, 0);

				log("Retrieving PersonB's phones by property");
				page = phoneRepository.select(new SelectDetail<Phone>(personB.getId(), new ByText("1122", "number")));
				assertValidDataPage(page, 1);

				log("Retrieving PersonB's phones with paging");
				page = phoneRepository.select(new SelectDetail<Phone>(personB.getId()).page(0, 1));
				assertValidDataPage(page, 1, 2);

				return null;
			}
		});
	}

	private void assertValidPerson(Person person, String name) {
		assertNotNull("null person", person);
		assertNotNull("null person.id", person.getId());
		assertNotNull("null person.name", person.getName());
		assertEquals("incorrect person.name", name, person.getName());
	}

	private void assertValidPhone(Phone phone, String number) {
		assertNotNull("null phone", phone);
		assertNotNull("null phone.id", phone.getId());
		assertNotNull("null phone.number", phone.getNumber());
		assertEquals("incorrect phone.number", number, phone.getNumber());
	}

	private void assertValidPet(Pet pet, String name, Person owner) {
		assertNotNull("null pet", pet);
		assertNotNull("null pet.id", pet.getId());
		assertNotNull("null pet.name", pet.getName());
		assertEquals("incorrect pet.name", name, pet.getName());
		assertNotNull("null pet.owner", pet.getOwner());
		assertEquals("incorrect pet.owner.id", owner.getId(), pet.getOwner().getId());
	}

	private void assertValidDataPage(DataPage<?> page, int pageSize) {
		assertValidDataPage(page, pageSize, null);
	}

	private void assertValidDataPage(DataPage<?> page, int pageSize, long totalSize) {
		assertValidDataPage(page, pageSize, Long.valueOf(totalSize));
	}

	private void assertValidDataPage(DataPage<?> page, int pageSize, Long totalSize) {
		assertEquals("page size should be: " + pageSize, pageSize, page.getData().size());
		assertEquals("total size should be: " + totalSize, totalSize, page.getTotal());
	}
}
