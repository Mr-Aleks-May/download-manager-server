package com.mralexmay.projects.download_manager.server.v_1.repositary;

import com.mralexmay.projects.download_manager.server.v_1.model.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
}
