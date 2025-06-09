package prateek_gupta.SampleProject.core;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import prateek_gupta.SampleProject.db.entities.Table1Entity;

@Repository
public interface ConfigurationsRepository extends
        JpaRepositoryImplementation<Configurations, Integer> {

}
