package com.johannesbrodwall.hellodocumentdb.person.impl;

import java.util.UUID;

import com.johannesbrodwall.hellodocumentdb.person.Person;
import com.johannesbrodwall.hellodocumentdb.person.PersonRepository;
import com.johannesbrodwall.winter.ExceptionUtil;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.RequestOptions;
import com.microsoft.azure.documentdb.ResourceResponse;

public class CosmosPersonRepository implements PersonRepository {

    private final DocumentClient documentClient;
	private String database;

    public CosmosPersonRepository(DocumentClient documentClient, String database) {
		this.documentClient = documentClient;
		this.database = database;
	}

    @Override
	public void save(Person person) {
    	if (person.getId() == null) {
    		person.setId(UUID.randomUUID().toString());
    	}
        ensureCollectionExists(getCollectionLink());

        try {
			documentClient.createDocument(getCollectionLink(), person, new RequestOptions(), false);
		} catch (DocumentClientException e) {
			throw ExceptionUtil.soften(e);
		}
	}

	@Override
	public Person findOne(String id) {
		try {
			String documentLink = getDocumentLink(id);
			ResourceResponse<Document> document = documentClient.readDocument(documentLink, new RequestOptions());
			Document resource = document.getResource();
			Person person = new Person();
			person.setId(resource.getId());
			person.setName(resource.getString("name"));
			return person;
		} catch (DocumentClientException e) {
			throw ExceptionUtil.soften(e);
		}
	}

	private void ensureCollectionExists(String collectionLink) {
		ensureDatabaseExists();

        try {
			documentClient.readCollection(collectionLink, null);
		} catch (DocumentClientException e) {
			if (e.getStatusCode() == 404) {
				DocumentCollection collectionInfo = new DocumentCollection();
				collectionInfo.setId("person");
				try {
					documentClient.createCollection(getDatabaseLink(), collectionInfo, new RequestOptions());
				} catch (DocumentClientException e1) {
					throw ExceptionUtil.soften(e1);
				}
			} else {
				throw ExceptionUtil.soften(e);
			}
		}

	}

	private void ensureDatabaseExists() {
		try {
			documentClient.readDatabase(getDatabaseLink(), null).getResource();
		} catch (DocumentClientException e) {
			if (e.getStatusCode() == 404) {
				Database database = new Database();
				database.setId(this.database);
				try {
					documentClient.createDatabase(database, new RequestOptions());
				} catch (DocumentClientException e1) {
					throw ExceptionUtil.soften(e1);
				}
			} else {
				throw ExceptionUtil.soften(e);
			}
		}

	}

	private String getDatabaseLink() {
		return String.format("/dbs/%s", database);
	}

	private String getCollectionLink() {
		return String.format("/dbs/%s/colls/%s", database, "person");
	}

	private String getDocumentLink(String id) {
		return String.format("/dbs/%s/colls/%s/docs/%s", database, "person", id);
	}

}
