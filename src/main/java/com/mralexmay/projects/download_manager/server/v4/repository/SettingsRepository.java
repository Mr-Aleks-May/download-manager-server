package com.mralexmay.projects.download_manager.server.v4.repository;

import com.mralexmay.projects.download_manager.server.v4.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
}
