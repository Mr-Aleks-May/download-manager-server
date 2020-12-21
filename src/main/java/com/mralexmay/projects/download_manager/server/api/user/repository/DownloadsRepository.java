package com.mralexmay.projects.download_manager.server.api.user.repository;

import com.mralexmay.projects.download_manager.server.api.user.model.Download;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadsRepository extends JpaRepository<Download, Long> {
}
