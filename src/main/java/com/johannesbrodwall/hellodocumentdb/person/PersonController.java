package com.johannesbrodwall.hellodocumentdb.person;

import com.johannesbrodwall.winter.http.requests.HttpActionSelector;
import com.johannesbrodwall.winter.http.requests.HttpActionServlet;
import com.johannesbrodwall.winter.http.requests.HttpResponder;

public class PersonController extends HttpActionServlet implements HttpResponder {

    private PersonControllerContext context;

    public PersonController(PersonControllerContext context) {
        this.context = context;
    }

    @Override
    public void handle(HttpActionSelector selector) {
        selector.onPost("/", action -> {
            action.returnObject(postPerson(action.parameter("name")));
        });
        selector.onGet("/{id}", e -> {
            e.returnObject(getPerson(e.pathVariable("id")));
        });
    }

    public Person postPerson(String name) {
        Person person = new Person();
        person.setName(name);
        context.getPersonRepository().save(person);
        return person;
    }

    public Person getPerson(String id) {
        return context.getPersonRepository().findOne(id);
    }

}
