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
