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
