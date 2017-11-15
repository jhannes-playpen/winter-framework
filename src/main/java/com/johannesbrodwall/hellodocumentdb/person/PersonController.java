package com.johannesbrodwall.hellodocumentdb.person;

import javax.servlet.ServletException;

import com.johannesbrodwall.winter.http.server.HttpActionSelector;
import com.johannesbrodwall.winter.http.server.HttpActionServlet;

public class PersonController extends HttpActionServlet {

	private PersonControllerContext context;

	@Override
	public void init() throws ServletException {
		this.context = (PersonControllerContext) getServletContext().getAttribute("config");
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
