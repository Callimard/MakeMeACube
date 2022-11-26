package org.callimard.makemeacube.entities.sql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByMail(String mail);

    Optional<User> findByPseudo(String pseudo);
}