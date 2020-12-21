package com.mralexmay.projects.download_manager.server.api.store.plugin.repository;

import com.mralexmay.projects.download_manager.server.api.store.plugin.model.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PluginRepository extends JpaRepository<Plugin, Long> {
}
