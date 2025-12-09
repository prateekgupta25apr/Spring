package prateek_gupta.SampleProject.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.base.Context;
import prateek_gupta.SampleProject.multitenancy.TenantContext;
import prateek_gupta.SampleProject.prateek_gupta.*;
import prateek_gupta.SampleProject.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static prateek_gupta.SampleProject.utils.Util.getJsonNode;

@Service
public class CoreServiceImpl implements CoreService {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ConfigurationsRepository configurationsRepository;

    @Value("${test:}")
    String test;

    @Override
    public ObjectNode test(String testData) throws ServiceException {
        ObjectNode response = Util.getObjectMapper().createObjectNode();
        try {
            Object result = entityManager.createNativeQuery("select schema();")
                    .getSingleResult();
            response.set("test", getJsonNode(Init.getConfiguration("test",test)));
            response.put("test_data", testData);
            response.put("schema_db", String.valueOf(result));
            response.put("tenant_schema_name",
                    TenantContext.getCurrentTenant().getSchemaName());
            response.put("context_user_id",
                    Context.getCurrentContext().userId);
            response.set("configuration_properties",
                    getJsonNode(Init.configuration_properties));

            response.set("exception_messages",
                    getJsonNode(Utils.loadPropertiesFromFile(
                            Init.projectDir +
                                    "resources\\ServiceExceptionMessages.properties",
                            new ArrayList<>(),
                            new ArrayList<>(),true)));
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException(ServiceException.ExceptionType.DB_ERROR);
        }
        return response;
    }

    @Override
    @PostConstructMethod("*")
    public void loadConfigValueFromDB(String keyList) throws ServiceException {
        try {
            List<String> keys= Arrays.asList(keyList.split(","));
            for (Configurations configurations : configurationsRepository.findAll())
                if (keyList.equals("*")||keys.contains(configurations.getKey()))
                    Init.configuration_properties.put(
                        configurations.getKey(), configurations.getValue());
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
    }
}
