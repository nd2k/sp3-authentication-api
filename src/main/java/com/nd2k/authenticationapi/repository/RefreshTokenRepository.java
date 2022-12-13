package com.nd2k.authenticationapi.repository;

import com.nd2k.authenticationapi.model.auth.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
}
