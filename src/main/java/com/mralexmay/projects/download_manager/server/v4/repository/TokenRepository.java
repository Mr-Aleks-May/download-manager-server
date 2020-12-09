package com.mralexmay.projects.download_manager.server.v4.repository;

import com.mralexmay.projects.download_manager.server.v4.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
