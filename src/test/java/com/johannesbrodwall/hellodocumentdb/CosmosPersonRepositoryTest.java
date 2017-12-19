package com.johannesbrodwall.hellodocumentdb;

import com.johannesbrodwall.hellodocumentdb.HelloApplicationContext;
import com.johannesbrodwall.hellodocumentdb.person.PersonRepository;
import com.johannesbrodwall.winter.config.PropertySource;

public class CosmosPersonRepositoryTest extends AbstractPersonRepositoryTest {

    private HelloApplicationContext context = new HelloApplicationContext(PropertySource.create("mongo,unittest"));

    private PersonRepository repository = context.getCosmosPersonRepository();

    @Override
    public PersonRepository getRepository() {
        return repository;
    }

}
