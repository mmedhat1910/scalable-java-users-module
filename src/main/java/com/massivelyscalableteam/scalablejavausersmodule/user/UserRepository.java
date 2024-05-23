package com.massivelyscalableteam.scalablejavausersmodule.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
    List<User> findByEmail(String email);
    User findBySession(String session);

    User deleteBySession(String session);

}
