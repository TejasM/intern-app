package org.jboss.tools.examples.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.tools.examples.model.Project;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class ProjectRegistration {

   @Inject
   private Logger logProject;

   @Inject
   private EntityManager emProject;

   @Inject
   private Event<Project> projectEventSrc;

   private Project newProject;

   @Produces
   @Named
   public Project getNewProject() {
      return newProject;
   }

   public void register() throws Exception {
      logProject.info("Adding New Project called " + newProject.getName());
      emProject.persist(newProject);
      projectEventSrc.fire(newProject);
      initNewProject();
   }

   @PostConstruct
   public void initNewProject() {
      newProject = new Project();
   }
}
