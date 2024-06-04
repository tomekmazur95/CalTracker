package com.crud.api.repository;

import com.crud.api.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserInfoId(Long id);

    boolean existsByUserInfoId(Long id);
}
