package com.johannesbrodwall.hellodocumentdb.person.impl;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.johannesbrodwall.hellodocumentdb.person.Person;
import com.johannesbrodwall.hellodocumentdb.person.PersonRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoPersonRepository implements PersonRepository {

    private MongoCollection<Document> collection;

    public MongoPersonRepository(MongoDatabase mongoDatabase) {
        collection = mongoDatabase.getCollection("person");
    }

    @Override
    public void save(Person person) {
        Document document = new Document()
                .append("name", person.getName());
        collection.insertOne(document);
        person.setId(document.getObjectId("_id").toString());
    }

    @Override
    public Person findOne(String id) {
        Document document = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        Person person = new Person();
        person.setId(document.getObjectId("_id").toString());
        person.setName(document.getString("name"));
        return person;
    }

}
