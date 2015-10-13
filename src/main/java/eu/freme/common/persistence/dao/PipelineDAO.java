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
package eu.freme.common.persistence.dao;

import eu.freme.common.persistence.model.Pipeline;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gerald Haesendonck
 */
@Component
public class PipelineDAO extends OwnedResourceDAO<Pipeline> {
	@Override
	public String className() {
		return Pipeline.class.getSimpleName();
	}

	private synchronized String getNewId() {
		long currentTime = System.currentTimeMillis();
		return Long.toString(currentTime, Character.MAX_RADIX);
	}

	@Override
	public void save(Pipeline pipeline) {
		if (pipeline.getId() == null) {
			pipeline.setId(getNewId());
		}
		super.save(pipeline);
	}

	/**
	 * Deletes pipelines that are older than one week.
	 */
	public void clean() {
		long currentTime = System.currentTimeMillis();
		long oneWeek = 7 * 24 * 60 * 60 * 1000;
		List<Pipeline> pipelinesToDelete = new ArrayList<>();

		// collect pipelines older than one week
		for (Pipeline pipeline : repository.findAll()) {
			if (!pipeline.isPersistent()) {
				String id = pipeline.getId();
				long creationTime = Long.parseLong(id, Character.MAX_RADIX);
				if (currentTime - oneWeek > creationTime) {
					pipelinesToDelete.add(pipeline);
				}
			}
		}

		// and now delete them
		repository.delete(pipelinesToDelete);
	}
}
