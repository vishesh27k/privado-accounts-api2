package ai.privado.demo.accounts.service.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.privado.demo.accounts.service.entity.UserE;

@Repository
public interface UsersR extends JpaRepository<UserE, String> {

	Optional<UserE> findByEmail(String email);
}
