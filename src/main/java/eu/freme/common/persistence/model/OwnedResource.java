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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import eu.freme.common.exception.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Jonathan Sauder jsauder@campus.tu-berlin.de
 */

@MappedSuperclass
public class OwnedResource implements Serializable {

    public enum Visibility {
        PRIVATE,
        PUBLIC;
        public static Visibility getByString(String value) throws BadRequestException {
            if(value!=null && value.toLowerCase().equals("private"))
                return PRIVATE;
            if(value!=null && !value.toLowerCase().equals("public"))
                throw new BadRequestException("Wrong value for visibility level: \""+value+"\". Has to be either \"private\" or \"public\".");
            return PUBLIC;
        }
    }

    @Id
    private long id;

    private long creationTime;

    @ManyToOne(fetch = FetchType.EAGER) //(optional=false,targetEntity = User.class)
    private User owner;

    private Visibility visibility;

    public OwnedResource(){}

    public OwnedResource(long id, User owner, Visibility visibility) {
        this.creationTime = System.currentTimeMillis();
        this.id = id;
        this.owner = owner;
        this.visibility = visibility;
    }

    public OwnedResource(long id, Visibility visibility) throws AccessDeniedException{
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken)
            throw new AccessDeniedException("Could not create resource: The anonymous user can not own any resource. You have to be logged in to create a resource.");
        this.owner = (User) authentication.getPrincipal();
        this.id = id;
        this.visibility = visibility;
        this.creationTime = System.currentTimeMillis();
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String toString(){
        return "OwnedResource[id="+id+", owner="+owner.toString()+", visibility="+ visibility.toString()+"]";
    }

}
