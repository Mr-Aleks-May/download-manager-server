package com.mralexmay.projects.download_manager.server.v.repository;

import com.mralexmay.projects.download_manager.server.v.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
}
