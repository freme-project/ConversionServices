package eu.freme.common.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eu.freme.common.conversion.SerializationFormatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;

import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.FREMEHttpException;
import eu.freme.common.persistence.dao.OwnedResourceDAO;
import eu.freme.common.persistence.dao.UserDAO;
import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.User;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 12.01.2016.
 */
@RestController
public abstract class OwnedResourceManagingController<Entity extends OwnedResource> extends BaseRestController {

    //public static final String relativeManagePath = "/manage";
    public static final String visibilityParameterName = "visibility";
    public static final String newOwnerParameterName = "owner";
    public static final String descriptionParameterName = "description";


    @Autowired
    OwnedResourceDAO<Entity> entityDAO;

    @Autowired
    UserDAO userDAO;
    
    /**
     * Implementations of this method have to create a new entity object of the class Entity.
     * Http body, parameters and headers should be evaluated before forwarding to any Entity constructor.
     *
     * @param body http request body
     * @param parameters http request parameters
     * @param headers http request headers
     * @return the new entity object
     * @throws BadRequestException
     */
    protected abstract Entity createEntity(String body, Map<String, String> parameters, Map<String, String> headers) throws BadRequestException;
    
    /**
     * Implementations of this method have to update an entity object of the class Entity.
     * Http body, parameters and headers should be evaluated before forwarding to any method which manipulates the entity object.
     *
     * @param entity the entity object which should be updated
     * @param body http request body
     * @param parameters http request parameters
     * @param headers http request headers
     * @throws BadRequestException
     */
    protected abstract void updateEntity(Entity entity, String body, Map<String, String> parameters, Map<String, String> headers) throws BadRequestException;

    protected void preDelete(Entity entity){
        // empty
    }

    public OwnedResourceDAO<Entity> getEntityDAO() {
        return entityDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    @RequestMapping( method = RequestMethod.POST)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> addEntity(
            @RequestParam(value = visibilityParameterName, required = false) String visibility,
            @RequestParam(value = descriptionParameterName, required = false) String description,
            @RequestParam Map<String, String> allParams,
            @RequestHeader Map<String, String> allHeaders,
            @RequestBody(required = false) String postBody
    ){
        try {

            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();
            if(authentication instanceof AnonymousAuthenticationToken)
                throw new AccessDeniedException("Access denied");

            Entity entity = createEntity(postBody, allParams, allHeaders);

            if(entity.getOwner()==null)
                entity.setCurrentUserAsOwner();

            if(!Strings.isNullOrEmpty(visibility)){
                entity.setVisibility(OwnedResource.Visibility.getByString(visibility));
            }

            if(!Strings.isNullOrEmpty(description)){
                entity.setDescription(description);
            }

            entity = entityDAO.save(entity);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", SerializationFormatMapper.JSON);
            return new ResponseEntity<>(entity.toJson(), responseHeaders, HttpStatus.OK);
        } catch (AccessDeniedException ex) {
            logger.error(ex.getMessage(), ex);
            throw new eu.freme.common.exception.AccessDeniedException(ex.getMessage());
        } catch (BadRequestException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (org.json.JSONException ex) {
            logger.error(ex.getMessage(), ex);
            throw new BadRequestException(ex.getMessage());
        }catch (FREMEHttpException ex){
            logger.error(ex.getMessage());
            throw ex;
        }catch(Exception ex){
            logger.error(ex.getMessage());
            throw new FREMEHttpException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/{identifier}",  method = RequestMethod.POST)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> addEntity2(
            @PathVariable("identifier") String identifier,
            @RequestParam(value = visibilityParameterName, required = false) String visibility,
            @RequestParam(value = descriptionParameterName, required = false) String description,
            @RequestParam Map<String, String> allParams,
            @RequestHeader Map<String, String> allHeaders,
            @RequestBody(required = false) String postBody
    ){
        if(allParams == null){
            allParams = new HashMap<>();
        }
        allParams.put(entityDAO.getIdentifierName(), identifier);
        return addEntity(visibility, description, allParams, allHeaders, postBody);
    }

    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> getEntityById(
            @PathVariable("identifier") String identifier
    ){
        try {
            Entity entity = entityDAO.findOneByIdentifier(identifier);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", SerializationFormatMapper.JSON);
            return new ResponseEntity<>(entity.toJson(), responseHeaders, HttpStatus.OK);
        }catch (AccessDeniedException ex) {
            logger.error(ex.getMessage(), ex);
            throw new eu.freme.common.exception.AccessDeniedException(ex.getMessage());
        } catch (BadRequestException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (FREMEHttpException ex){
            logger.error(ex.getMessage());
            throw ex;
        }catch(Exception ex){
            logger.error(ex.getMessage());
            throw new FREMEHttpException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/{identifier}", method = RequestMethod.PUT)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> putEntityById(
            @PathVariable("identifier") String identifier,
            @RequestParam(value = visibilityParameterName, required = false) String visibility,
            @RequestParam(value = descriptionParameterName, required = false) String description,
            @RequestParam(value = newOwnerParameterName, required = false) String ownerName,
            @RequestParam Map<String, String> allParams,
            @RequestHeader Map<String, String> allHeaders,
            @RequestBody(required = false) String postBody
    ){
        try {
            Entity entity = entityDAO.findOneByIdentifier(identifier);

            if(!entityDAO.hasWriteAccess(entity))
                throw new AccessDeniedException("Access denied");

            updateEntity(entity, postBody, allParams, allHeaders);

            if(!Strings.isNullOrEmpty(visibility)){
                entity.setVisibility(OwnedResource.Visibility.getByString(visibility));
            }

            if(!Strings.isNullOrEmpty(description)){
                entity.setDescription(description);
            }

            entityDAO.save(entity);

            if (!Strings.isNullOrEmpty(ownerName)) {
                User owner = userDAO.getRepository().findOneByName(ownerName);
                if (owner == null)
                    throw new BadRequestException(
                            "Can not change owner of the entity. User \""
                                    + ownerName + "\" does not exist.");
                entity = entityDAO.updateOwner(entity, owner);
            }
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", SerializationFormatMapper.JSON);
            return new ResponseEntity<>(entity.toJson(), responseHeaders, HttpStatus.OK);
        }catch (AccessDeniedException ex) {
            logger.error(ex.getMessage(), ex);
            throw new eu.freme.common.exception.AccessDeniedException(ex.getMessage());
        } catch (BadRequestException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (org.json.JSONException ex) {
            logger.error(ex.getMessage(), ex);
            throw new BadRequestException(
                    "The JSON object is incorrectly formatted. Problem description: "
                            + ex.getMessage());
        } catch (FREMEHttpException ex){
            logger.error(ex.getMessage());
            throw ex;
        }catch(Exception ex){
            logger.error(ex.getMessage());
            throw new FREMEHttpException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/{identifier}", method = RequestMethod.DELETE)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> deleteEntityById(
            @PathVariable("identifier") String identifier
    ){
        try {
            Entity entity = entityDAO.findOneByIdentifier(identifier);
            if(!entityDAO.hasWriteAccess(entity))
                throw new AccessDeniedException("Access denied");
            preDelete(entity);
            entityDAO.delete(entity);
            return new ResponseEntity<>("The " + entityDAO.tableName() + ": " + entity.getIdentifier() + " was removed sucessfully.", HttpStatus.OK);
        }catch (AccessDeniedException ex) {
            logger.error(ex.getMessage(), ex);
            throw new eu.freme.common.exception.AccessDeniedException(ex.getMessage());
        } catch (BadRequestException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (FREMEHttpException ex){
            logger.error(ex.getMessage());
            throw ex;
        }catch(Exception ex){
            logger.error(ex.getMessage());
            throw new FREMEHttpException(ex.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> getAllEntities(
    ) {
        try {
            List<Entity> entities = entityDAO.findAllReadAccessible();
            String serialization = entities.stream()
                    .map(p -> {
                        try {
                            return p.toJson();
                        } catch (JsonProcessingException e) {
                            throw new FREMEHttpException("Could not serialize entity with identifier=\""+p.getIdentifier()+"\" to JSON. "+e.getMessage());
                        }
                    })
                    .collect(Collectors.joining(",\n"));

            HttpHeaders responseHeaders = new HttpHeaders();
            //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //String serialization = ow.writeValueAsString(entities);
            responseHeaders.add("Content-Type", SerializationFormatMapper.JSON);
            return new ResponseEntity<>("["+serialization+"]", responseHeaders, HttpStatus.OK);
        } catch (FREMEHttpException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new FREMEHttpException(ex.getMessage());
        }
    }

}
