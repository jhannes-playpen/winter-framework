package com.johannesbrodwall.hellodocumentdb;

import com.johannesbrodwall.hellodocumentdb.HelloApplicationContext;
import com.johannesbrodwall.hellodocumentdb.person.PersonRepository;
import com.johannesbrodwall.winter.config.PropertySource;

public class MongoPersonRepositoryTest extends AbstractPersonRepositoryTest {

	private HelloApplicationContext context = new HelloApplicationContext(PropertySource.create("mongo,unittest"));

	private PersonRepository repository = context.getMongoPersonRepository();

	@Override
	public PersonRepository getRepository() {
		return repository;
	}

}
