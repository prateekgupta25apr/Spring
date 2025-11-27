package prateek_gupta.SampleProject.db.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import prateek_gupta.SampleProject.db.entities.Table1Entity;

@Repository
@Transactional
public interface Table1Repository extends JpaRepositoryImplementation<Table1Entity, Integer> {

    Table1Entity findByPrimaryKey(Integer primaryKey);

    @Modifying
    @Query("update Table1Entity set col1=:col1,col2=:col2 where primaryKey=:primaryKey")
    void updateData(Integer primaryKey,String col1,boolean col2);

    void deleteByPrimaryKey(Integer primaryKey);
}
