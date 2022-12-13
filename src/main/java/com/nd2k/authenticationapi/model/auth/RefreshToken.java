package com.nd2k.authenticationapi.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("Refresh-token")
public class RefreshToken {

    @Id
    private String id;
    @DocumentReference
    private User user;
}
