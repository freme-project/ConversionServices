package eu.freme.common.security.voter;

import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.User;
import eu.freme.common.persistence.tools.AccessLevelHelper;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
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
            if (authentication.getPrincipal().equals("anonymousUser")) {
                return ACCESS_DENIED;
            }

            User authenticatedUser = (User) authentication.getPrincipal();

            if (authenticatedUser.getRole().equals(User.roleAdmin)) {
                return ACCESS_GRANTED;
            } else if (casted.getVisibility().equals(OwnedResource.Visibility.PUBLIC) && accessLevelHelper.hasRead(attributes)) {
                return ACCESS_GRANTED;
            } else if (authenticatedUser.getName().equals(casted.getOwner().getName())) {
                return ACCESS_GRANTED;
            } else {
                return ACCESS_DENIED;
            }

        } else return ACCESS_ABSTAIN;

    }
}
