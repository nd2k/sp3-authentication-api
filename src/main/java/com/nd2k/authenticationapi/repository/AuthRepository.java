package com.nd2k.authenticationapi.repository;

import com.nd2k.authenticationapi.model.auth.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);
}
