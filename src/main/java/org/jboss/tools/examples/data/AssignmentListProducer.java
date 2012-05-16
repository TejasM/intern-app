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

import org.jboss.tools.examples.model.Assignment;

@RequestScoped
public class AssignmentListProducer {
   @Inject
   private EntityManager emAssignment;

   private List<Assignment> assignments;

   // @Named provides access the return value via the EL variable name "Assignments" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<Assignment> getAssignments() {
      return assignments;
   }

   public void onAssignmentListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Assignment Assignment) {
      retrieveAllAssignmentsOrderedByName();
   }

   @PostConstruct
   public void retrieveAllAssignmentsOrderedByName() {
      CriteriaBuilder cb = emAssignment.getCriteriaBuilder();
      CriteriaQuery<Assignment> criteria = cb.createQuery(Assignment.class);
      Root<Assignment> Assignment = criteria.from(Assignment.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(Assignment).orderBy(cb.asc(Assignment.get(Assignment_.name)));
      criteria.select(Assignment).orderBy(cb.asc(Assignment.get("AssignmentName")));
      assignments = emAssignment.createQuery(criteria).getResultList();
   }
}
