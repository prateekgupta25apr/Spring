package prateek_gupta.SampleProject.db.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.db.dao.Table1Repository;
import prateek_gupta.SampleProject.db.entities.Table1Entity;
import prateek_gupta.SampleProject.db.service.DBService;
import prateek_gupta.SampleProject.db.vo.Table1VO;
import prateek_gupta.SampleProject.multitenancy.TenantContext;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class DBServiceImpl implements DBService {

    private final Logger log = LoggerFactory.getLogger(DBServiceImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    Table1Repository table1Repository;

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

    @Override
    public Table1VO getTable1Details(Integer primaryKey) {
        log.info("getTable1Details started");
        try{
            Table1Entity entity= table1Repository.findByPrimaryKey(primaryKey);
            if (entity != null)
                return entity.toVO();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("getTable1Details ended");
        return null;
    }

    @Override
    public void saveTable1Details(String data) {
        log.info("saveTable1Details started");
        try{
                JSONObject jsonObject=JSONObject.fromObject(data);
                Gson gson=new Gson();
                Table1VO table1VO = gson.fromJson(
                        jsonObject.get("data").toString(), Table1VO.class);

            table1Repository.save(table1VO.toEntity());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("saveTable1Details ended");
    }
}
