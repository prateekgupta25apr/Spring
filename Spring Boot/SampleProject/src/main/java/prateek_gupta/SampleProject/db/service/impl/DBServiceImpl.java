package prateek_gupta.SampleProject.db.service.impl;

import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.db.dao.Table1AttachmentMappingRepository;
import prateek_gupta.SampleProject.db.dao.Table1Dao;
import prateek_gupta.SampleProject.db.dao.Table1Repository;
import prateek_gupta.SampleProject.db.entities.Table1AttachmentMappingEntity;
import prateek_gupta.SampleProject.db.entities.Table1Entity;
import prateek_gupta.SampleProject.db.service.DBService;
import prateek_gupta.SampleProject.db.vo.Table1VO;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

@Service
public class DBServiceImpl implements DBService {

    private final Logger log = LoggerFactory.getLogger(DBServiceImpl.class);

    @Autowired
    Table1Repository table1Repository;

    @Autowired
    Table1Dao table1Dao;

    @Autowired
    Table1AttachmentMappingRepository table1AttachmentMappingRepository;

    @Override
    public Table1VO getData(Integer primaryKey) throws ServiceException {
        log.info("Entering Service : getData()");
        try {
            Table1Entity entity = table1Repository.findByPrimaryKey(primaryKey);
            if (entity != null)
                return entity.toVO();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : getData()");
        return null;
    }

    @Override
    public void saveData(Object data) throws ServiceException {
        log.info("Entering Service : saveData()");
        try {
            JSONObject jsonObject = JSONObject.fromObject(data);
            Gson gson = new Gson();
            Table1VO table1VO = gson.fromJson(
                    jsonObject.toString(), Table1VO.class);

            table1Dao.saveData(table1VO.toEntity());
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : saveData()");
    }

    @Override
    public void updateData(Integer primaryKey, String col1, boolean col2)
            throws ServiceException {
        log.info("Entering Service : updateData()");
        try {
            table1Repository.updateData(primaryKey, col1, col2);
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : updateData()");
    }

    @Override
    public void partialUpdateData(Integer primaryKey, String col1, Boolean col2)
            throws ServiceException {
        log.info("Entering Service : partialUpdateData()");
        try {
            table1Dao.partialUpdateData(primaryKey, col1, col2);
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : partialUpdateData()");
    }

    @Override
    public void deleteData(Integer primaryKey) throws ServiceException {
        log.info("Entering Service : deleteData()");
        try {
            table1Repository.deleteByPrimaryKey(primaryKey);
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : deleteData()");
    }

    @Override
    public void addAttachment(
            Integer table1PrimaryKey, String attachmentPath) throws ServiceException {
        log.info("Entering Service : addAttachment()");
        try {
            Table1AttachmentMappingEntity entity = new Table1AttachmentMappingEntity();
            Table1Entity table1Entity = new Table1Entity();
            table1Entity.setPrimaryKey(table1PrimaryKey);
            entity.setTable1Entity(table1Entity);
            entity.setAttachmentPath(attachmentPath);
            table1AttachmentMappingRepository.save(entity);
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : addAttachment()");
    }

    @Override
    public String getAttachmentPath(Integer primaryKey) throws ServiceException {
        String attachmentPath;
        log.info("Entering Service : getAttachment()");
        try {
            Table1AttachmentMappingEntity entity =
                    table1AttachmentMappingRepository.findByPrimaryKey(primaryKey);
            attachmentPath= entity.getAttachmentPath();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting Service : getAttachment()");
        return attachmentPath;
    }
}
