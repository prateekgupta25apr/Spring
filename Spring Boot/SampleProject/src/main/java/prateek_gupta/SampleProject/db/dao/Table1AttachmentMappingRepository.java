package prateek_gupta.SampleProject.db.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import prateek_gupta.SampleProject.db.entities.Table1AttachmentMappingEntity;

@Repository
@Transactional
public interface Table1AttachmentMappingRepository extends JpaRepositoryImplementation<Table1AttachmentMappingEntity, Integer> {
    Table1AttachmentMappingEntity findByPrimaryKey(Integer primaryKey);
}
