package prateek_gupta.SampleProject.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.multitenancy.TenantContext;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class CoreServiceImpl implements CoreService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public ObjectNode test(String testData) throws ServiceException {
        ObjectNode response= Util.getObjectMapper().createObjectNode();
        try{
            Object result= entityManager.createNativeQuery("select schema();")
                    .getSingleResult();
            response.put("test_data",testData);
            response.put("schema_db",String.valueOf(result));
            response.put("schema_name",
                    TenantContext.getCurrentTenant().getSchemaName());
        }catch (Exception e){
            ServiceException.logException(e);
            throw new ServiceException(ServiceException.ExceptionType.DB_ERROR);
        }
        return response;
    }
}
