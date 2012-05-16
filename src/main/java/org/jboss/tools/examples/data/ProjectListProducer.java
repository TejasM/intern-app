package org.jboss.tools.examples.data;

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

import org.jboss.tools.examples.model.Project;

@RequestScoped
public class ProjectListProducer {
   @Inject
   private EntityManager emProject;

   private List<Project> projects;

   // @Named provides access the return value via the EL variable name "Projects" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<Project> getProjects() {
      return projects;
   }

   public void onProjectListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Project Project) {
      retrieveAllProjectsOrderedByName();
   }

   @PostConstruct
   public void retrieveAllProjectsOrderedByName() {
      CriteriaBuilder cb = emProject.getCriteriaBuilder();
      CriteriaQuery<Project> criteria = cb.createQuery(Project.class);
      Root<Project> Project = criteria.from(Project.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(Project).orderBy(cb.asc(Project.get(Project_.name)));
      criteria.select(Project).orderBy(cb.asc(Project.get("name")));
      projects = emProject.createQuery(criteria).getResultList();
   }
}