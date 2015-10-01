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

import eu.freme.common.persistence.model.Dataset;
import eu.freme.common.persistence.model.Template;
import eu.freme.common.persistence.model.Token;
import eu.freme.common.persistence.model.User;
import eu.freme.common.persistence.repository.DatasetRepository;
import eu.freme.common.persistence.repository.TemplateRepository;
import eu.freme.common.persistence.repository.TokenRepository;
import eu.freme.common.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@Component
public class UserDAO extends DAO<UserRepository, User> {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    DatasetRepository datasetRepository;

    @Autowired
    TemplateRepository templateRepository;

    public void delete(User entity){
        for( Token token : entity.getTokens() ){
            tokenRepository.delete(token);
        }

        for( Dataset dataset : entity.getDatasets() ){
            datasetRepository.delete(dataset);
        }

        // TODO: Why is entity.getTemplates() sometimes null??
        if(entity.getTemplates()!=null) {
            for (Template template : entity.getTemplates()) {
                templateRepository.delete(template);
            }
        }
        super.delete(entity);
    }
}
