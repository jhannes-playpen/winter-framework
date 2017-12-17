package com.johannesbrodwall.hellodocumentdb;

import java.util.HashMap;
import java.util.Map;

import com.johannesbrodwall.hellodocumentdb.person.PersonControllerContext;
import com.johannesbrodwall.hellodocumentdb.person.PersonRepository;
import com.johannesbrodwall.hellodocumentdb.person.impl.CosmosPersonRepository;
import com.johannesbrodwall.hellodocumentdb.person.impl.MongoPersonRepository;
import com.johannesbrodwall.winter.config.PropertySource;
import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class HelloApplicationContext implements PersonControllerContext {

	private final PropertySource props;

	public HelloApplicationContext(PropertySource propertySource) {
		this.props = propertySource;
	}

	@Override
	public PersonRepository getPersonRepository() {
		String databaseProvider = props.propertyChoice("personRepository", new String[] { "mongo", "cosmos" }, "mongo");
		if (databaseProvider.equals("mongo")) {
			return getMongoPersonRepository();
		} else {
			return getCosmosPersonRepository();
		}
	}

	PersonRepository getMongoPersonRepository() {
		return new MongoPersonRepository(getMongoClient().getDatabase(props.required("mongo.database")));
	}

	PersonRepository getCosmosPersonRepository() {
		return new CosmosPersonRepository(
				new DocumentClient(props.requiredUrl("cosmos.url"), props.required("cosmos.key"), new ConnectionPolicy(), ConsistencyLevel.Session),
				props.required("cosmos.database"));
	}

	private Map<String, MongoClient> mongoClientCache = new HashMap<>();

	private MongoClient getMongoClient() {
		String mongoUrl = props.property("mongo.url").orElse("mongodb://localhost:27017");
		return mongoClientCache.computeIfAbsent(mongoUrl, url -> new MongoClient(new MongoClientURI(url)));
	}

}
