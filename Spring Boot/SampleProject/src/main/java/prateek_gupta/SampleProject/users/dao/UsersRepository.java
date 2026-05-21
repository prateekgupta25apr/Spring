package prateek_gupta.SampleProject.users.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import prateek_gupta.SampleProject.users.entities.Users;

@Repository
public interface UsersRepository extends JpaRepositoryImplementation<Users, Integer> {

    Users findByUserId(Integer userId);
    Users findByEmail(String email);
}
