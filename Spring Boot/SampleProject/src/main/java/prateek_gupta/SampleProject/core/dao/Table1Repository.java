package prateek_gupta.SampleProject.core.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import prateek_gupta.SampleProject.core.entities.Table1Entity;

@Repository
public interface Table1Repository extends JpaRepositoryImplementation<Table1Entity, Integer> {
    Table1Entity findByPrimaryKey(Integer primaryKey);
}
