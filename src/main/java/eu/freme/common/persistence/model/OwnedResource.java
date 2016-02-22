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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.freme.common.exception.BadRequestException;

import eu.freme.common.exception.InternalServerErrorException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;

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
    @GeneratedValue
    private long id;

    private long creationTime;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.EAGER) //(optional=false,targetEntity = User.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    private Visibility visibility;

    public OwnedResource() throws AccessDeniedException{
        setCurrentUserAsOwner();
        this.creationTime = System.currentTimeMillis();
        this.visibility = Visibility.PUBLIC;
    }
    public OwnedResource(User owner){
        this.owner = owner;
        this.creationTime = System.currentTimeMillis();
        this.visibility = Visibility.PUBLIC;
    }

    public void setCurrentUserAsOwner() throws AccessDeniedException{
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken)
            throw new AccessDeniedException("Could not create resource: The anonymous user can not own any resource. You have to be logged in to create a resource.");
        this.owner = (User) authentication.getPrincipal();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public String getIdentifier(){
        return getId()+"";
    }

    @Override
	public String toString(){
        return "OwnedResource[id="+id+", owner="+owner.toString()+", visibility="+ visibility.toString()+"]";
    }

    public String toJson() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(this);
    }

    public static <T extends OwnedResource> T fromJson(String json, Class<T> entityClass)throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return entityClass.cast(mapper.readValue(json, entityClass));
    }

    public void preSave() throws BadRequestException{
        // empty
    }

    public void postFetch() throws InternalServerErrorException{
        // empty
    }
}
