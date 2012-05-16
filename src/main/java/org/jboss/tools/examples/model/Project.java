package org.jboss.tools.examples.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Project implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long projectId;

	@NotNull
	@Size(min = 1, max = 25)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String projectName;

	@NotNull
	private Long maxPeople;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long id) {
		this.projectId = id;
	}

	public String getName() {
		return projectName;
	}

	public void setName(String name) {
		this.projectName = name;
	}

	public Long getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(Long maxPeople) {
		this.maxPeople = maxPeople;
	}

}
