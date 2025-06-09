package prateek_gupta.SampleProject.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.multitenancy.TenantContext;
import prateek_gupta.SampleProject.prateek_gupta.Init;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static prateek_gupta.SampleProject.utils.Util.getJsonNode;

@Service
public class CoreServiceImpl implements CoreService {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ConfigurationsRepository configurationsRepository;

    @Value("${test}")
    String test;

    @Override
    public ObjectNode test(String testData) throws ServiceException {
        ObjectNode response = Util.getObjectMapper().createObjectNode();
        try {
            Object result = entityManager.createNativeQuery("select schema();")
                    .getSingleResult();
            response.put("test", test);
            response.put("test_data", testData);
            response.put("schema_db", String.valueOf(result));
            response.put("schema_name",
                    TenantContext.getCurrentTenant().getSchemaName());
            response.set("configuration_properties",
                    getJsonNode(Init.configuration_properties));
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException(ServiceException.ExceptionType.DB_ERROR);
        }
        return response;
    }

    @Override
    public void loadConfigValueFromDB() throws ServiceException {
        try {
            for (Configurations configurations : configurationsRepository.findAll())
                Init.configuration_properties.put(
                        configurations.getKey(), configurations.getValue());
        } catch (Exception e) {
            throw new ServiceException();
        }
    }
}
