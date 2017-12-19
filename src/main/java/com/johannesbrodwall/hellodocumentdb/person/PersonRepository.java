package com.johannesbrodwall.hellodocumentdb.person;

public interface PersonRepository {

    void save(Person person);

    Person findOne(String id);

}
