package com.hoderick.rabbithole.user.model;

import com.hoderick.rabbithole.audit.model.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends Auditable {

    @Id
    private String id; // This will be the Auth0 `sub` (e.g., "auth0|abc123")

    @Setter
    private String displayName;

    @Setter
    private String email;

    @Setter
    private String avatarKey;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String bio;

    public UserProfile(String id) {
        this.id = id;
    }
}
