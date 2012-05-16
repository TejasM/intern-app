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
import org.jboss.tools.examples.model.Assignment;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class AssignmentRegistration {

   @Inject
   private Logger logAssignment;

   @Inject
   private EntityManager emAssignment;

   @Inject
   private Event<Assignment> AssignmentEventSrc;

   private Assignment newAssignment;

   @Produces
   @Named
   public Assignment getNewAssignment() {
      return newAssignment;
   }

   public void register() throws Exception {
      logAssignment.info("Adding New Assignment for" + newAssignment.getName() + "for project" + newAssignment.getProjectName());
      emAssignment.persist(newAssignment);
      AssignmentEventSrc.fire(newAssignment);
      initNewAssignment();
   }

   @PostConstruct
   public void initNewAssignment() {
      newAssignment = new Assignment();
   }
}
