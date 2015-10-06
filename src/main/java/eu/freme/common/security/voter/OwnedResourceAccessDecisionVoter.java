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
package eu.freme.common.security.voter;

import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.User;
import eu.freme.common.persistence.tools.AccessLevelHelper;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class OwnedResourceAccessDecisionVoter implements AccessDecisionVoter<Object> {

    // TODO: Why does Autowire not work?
    //@Autowired
    //AccessLevelHelper accessLevelHelper;
    static AccessLevelHelper accessLevelHelper = new AccessLevelHelper();

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == OwnedResource.class;
    }

    @Override
    public int vote(Authentication authentication, Object object,
                    Collection<ConfigAttribute> attributes) {
        if (object instanceof OwnedResource) {
            OwnedResource casted= (OwnedResource) object;
         
            if(authentication instanceof AnonymousAuthenticationToken) {
                if (casted.getVisibility().equals(OwnedResource.Visibility.PUBLIC) && accessLevelHelper.hasRead(attributes) && !accessLevelHelper.hasWrite(attributes))
                    return ACCESS_GRANTED;
                else
                    return ACCESS_DENIED;
            }
            User authenticatedUser = (User) authentication.getPrincipal();

            if (authenticatedUser.getRole().equals(User.roleAdmin)) {
                return ACCESS_GRANTED;
            } else if (casted.getVisibility().equals(OwnedResource.Visibility.PUBLIC) && accessLevelHelper.hasRead(attributes) && !accessLevelHelper.hasWrite(attributes)) {
                return ACCESS_GRANTED;
            } else if (authenticatedUser.getName().equals(casted.getOwner().getName())) {
                return ACCESS_GRANTED;
            } else {
                return ACCESS_DENIED;
            }

        } else return ACCESS_ABSTAIN;

    }
}
