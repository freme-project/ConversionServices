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

import eu.freme.common.persistence.model.Template;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@Component
public class TemplateDAO extends OwnedResourceDAO<Template> {

    public String getNewId() {
        int newId = 0;
        if(repository.count()>0) {
            Iterator results = entityManager.createQuery("select max(template.id) from Template template").getResultList().iterator();
            if (results.hasNext()) {
                String result = (String)results.next();
                newId = Integer.parseInt(result);
            }else{
                logger.error("Could not determine the maximal template id value");
            }
        }
        newId++;
        logger.debug("template newId: "+newId);
        return newId+"";
    }

    @Override
    public String className() {
        return Template.class.getSimpleName();
    }

    public void save(Template template){
        // is it a new one?
        if(template.getId()== null){
            template.setId(getNewId()+"");
        }
        super.save(template);
    }

}
