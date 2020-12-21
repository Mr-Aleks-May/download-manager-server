package com.mralexmay.projects.download_manager.server.api.user.repository;

import com.mralexmay.projects.download_manager.server.api.user.model.UserPlugin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPluginsRepository extends JpaRepository<UserPlugin, Long> {
}
