package com.mralexmay.projects.download_manager.server.v4.repository;

import com.mralexmay.projects.download_manager.server.v4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
