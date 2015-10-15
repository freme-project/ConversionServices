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

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author Gerald Haesendonck
 */
@Entity
@Table(name = "pipeline")
public class Pipeline extends OwnedResource {
	private String label;
	@Lob
	private String description;
	@Lob
	private String serializedRequests;

	private boolean persist;	// true = persist forever; false = persist for (at least) one week.

	public Pipeline() {}

	public Pipeline(final User owner, final Visibility visibility, final String label, final String description, final String serializedRequests, boolean persist) {
		super(-1, owner, visibility);
		this.label = label;
		this.description = description;
		this.serializedRequests = serializedRequests;
		this.persist = persist;
	}

	public Pipeline(final Visibility visibility, final String label, final String description, final String serializedRequests, boolean persist) {
		super(-1, visibility);
		this.label = label;
		this.description = description;
		this.serializedRequests = serializedRequests;
		this.persist = persist;
	}

	public String getSerializedRequests() {
		return serializedRequests;
	}

	public void setSerializedRequests(String serializedRequests) {
		this.serializedRequests = serializedRequests;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}

	public boolean isPersistent() {
		return persist;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Pipeline pipeline = (Pipeline) o;

		if (persist != pipeline.persist) return false;
		return serializedRequests.equals(pipeline.serializedRequests);

	}

	@Override
	public int hashCode() {
		int result = serializedRequests.hashCode();
		result = 31 * result + (persist ? 1 : 0);
		return result;
	}
}
