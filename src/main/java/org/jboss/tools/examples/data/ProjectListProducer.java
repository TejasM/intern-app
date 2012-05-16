package org.jboss.tools.examples.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.tools.examples.model.Assignment;
import org.jboss.tools.examples.model.Project;

@RequestScoped
public class ProjectListProducer {
	@Inject
	private EntityManager emProject;
	
	@Inject
	private EntityManager emAssignment;

	private List<Project> projects;

	private List<String> projectsNames;

	private List<Assignment> assignments2;

	// @Named provides access the return value via the EL variable name
	// "Projects" in the UI (e.g.,
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Project> getProjects() {
		return projects;
	}

	@Produces
	@Named
	public List<String> getProjectsNames() {
		return projectsNames;
	}

	public void onProjectListChanged(
			@Observes(notifyObserver = Reception.IF_EXISTS) final Project Project) {
		retrieveAllProjectsOrderedByName();
	}

	@PostConstruct
	public void retrieveAllProjectsOrderedByName() {
		CriteriaBuilder cb = emProject.getCriteriaBuilder();
		CriteriaQuery<Project> criteria = cb.createQuery(Project.class);
		Root<Project> Project = criteria.from(Project.class);
		// Swap criteria statements if you would like to try out type-safe
		// criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(Project).orderBy(cb.asc(Project.get(Project_.name)));
		criteria.select(Project).orderBy(cb.asc(Project.get("projectName")));
		projects = emProject.createQuery(criteria).getResultList();
		toNames();
	}

	private void toNames() {
		// TODO Auto-generated method stub
		if (projectsNames == null) {
			projectsNames = new ArrayList<String>();
		}
		projectsNames.clear();
		// TODO Auto-generated method stub
		for (int i = 0; i < projects.size(); i++) {
			CriteriaBuilder cb = emAssignment.getCriteriaBuilder();
			CriteriaQuery<Assignment> criteria = cb
					.createQuery(Assignment.class);
			Root<Assignment> Assignment = criteria.from(Assignment.class);
			// Swap criteria statements if you would like to try out type-safe
			// criteria queries, a new
			// feature in JPA 2.0
			// criteria.select(Assignment).orderBy(cb.asc(Assignment.get(Assignment_.name)));
			criteria.select(Assignment).equals(projects.get(i));
			assignments2 = emAssignment.createQuery(criteria).getResultList();
			if(assignments2.size() < projects.get(i).getMaxPeople())
				projectsNames.add(projects.get(i).getName());
		}
	}

}
