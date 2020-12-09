package com.mralexmay.projects.download_manager.server.v.repository;

import com.mralexmay.projects.download_manager.server.v.model.download.DownloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadsRepository extends JpaRepository<DownloadEntity, Long> {
}
