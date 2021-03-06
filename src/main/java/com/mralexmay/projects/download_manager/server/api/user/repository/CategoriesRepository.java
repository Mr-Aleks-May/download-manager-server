package com.mralexmay.projects.download_manager.server.api.user.repository;

import com.mralexmay.projects.download_manager.server.api.user.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}
