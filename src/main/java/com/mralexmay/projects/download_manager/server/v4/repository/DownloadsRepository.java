package com.mralexmay.projects.download_manager.server.v4.repository;

import com.mralexmay.projects.download_manager.server.v4.model.DownloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadsRepository extends JpaRepository<DownloadEntity, Long> {
}
