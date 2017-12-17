package com.johannesbrodwall.hellodocumentdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import org.junit.Test;

import com.johannesbrodwall.hellodocumentdb.person.Person;
import com.johannesbrodwall.hellodocumentdb.person.PersonRepository;

public abstract class AbstractPersonRepositoryTest {

	private Random random = new Random();

	public abstract PersonRepository getRepository();

	@Test
	public void shouldRetrieveSavedPerson() throws Exception {
		Person person = new Person();
		person.setName(sampleName());

		getRepository().save(person);
		assertThat(person.getId()).isNotNull();

		assertThat(getRepository().findOne(person.getId()))
			.isEqualToComparingFieldByField(person);
	}

	private String sampleName() {
		return pickOne(new String[] { "Sondre", "Johannes", "Trygve", "Trond", "Kai Boris", "Markus" });
	}

	private <T> T pickOne(T[] options) {
		return options[random.nextInt(options.length)];
	}
}