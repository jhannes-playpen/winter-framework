package com.johannesbrodwall.winter.hellodocumentdb;

import com.johannesbrodwall.hellodocumentdb.person.PersonRepository;
import com.johannesbrodwall.winter.config.PropertySource;
import com.johannesbrodwall.winter.hellodocumentdb.HelloApplicationContext;

public class CosmosPersonRepositoryTest extends AbstractPersonRepositoryTest {

	private HelloApplicationContext context = new HelloApplicationContext(PropertySource.create("mongo,unittest"));

	private PersonRepository repository = context.getCosmosPersonRepository();

	@Override
	public PersonRepository getRepository() {
		return repository;
	}

}
