/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
@Entity
@Table(name = "user")
public class User {

	public static final String roleUser = "ROLE_USER";
	public static final String roleAdmin = "ROLE_ADMIN";

	@Id
	@Column(name = "name")
	private String name;

	@JsonIgnore
	private String password;

	private String role;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Token> tokens;

	@JsonIgnore
	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Dataset> datasets;

	@JsonIgnore
	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Template> templates;

	@JsonIgnore
	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Pipeline> pipelines;

	protected User() {
	}

	public User(String name, String password, String role) {
		this.name = name;
		this.password = password;
		this.role = role;

		tokens = new ArrayList<>();
		datasets = new ArrayList<>();
		pipelines = new ArrayList<>();
		templates = new ArrayList<>();
	}

	@Override
	public String toString() {
		return String.format("User[name=%s, role=%s]", name, role);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public List<Dataset> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}

	public List<Template> getTemplates() { return templates; }

	public void setTemplates(List<Template> templates) { this.templates = templates; }

	public List<Pipeline> getPipelines() {
		return pipelines;
	}

	public void setPipelines(List<Pipeline> pipelines) {
		this.pipelines = pipelines;
	}

	@Override
	public boolean equals(Object o){
		if(!(o instanceof User))
			return false;
		User casted = (User) o;
		return casted.getName().equals(this.getName());
	}

	@Override
	public int hashCode(){
		return this.getName().hashCode();
	}
}
