package ai.privado.demo.accounts.service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.privado.demo.accounts.service.entity.SessionE;

@Repository
public interface SessionsR extends JpaRepository<SessionE, String> {

}
