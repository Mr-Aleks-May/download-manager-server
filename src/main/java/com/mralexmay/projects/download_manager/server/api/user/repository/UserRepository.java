package com.mralexmay.projects.download_manager.server.api.user.repository;

import com.mralexmay.projects.download_manager.server.commons.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
