package com.mralexmay.projects.download_manager.server.api.user.repository;

import com.mralexmay.projects.download_manager.server.commons.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
