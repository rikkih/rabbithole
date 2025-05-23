package com.hoderick.rabbithole.user.repository;

import com.hoderick.rabbithole.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
