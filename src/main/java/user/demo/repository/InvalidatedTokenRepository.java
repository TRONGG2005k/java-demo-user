package user.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user.demo.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
