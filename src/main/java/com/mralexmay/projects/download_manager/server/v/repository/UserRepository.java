package com.mralexmay.projects.download_manager.server.v.repository;

import com.mralexmay.projects.download_manager.server.v.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
