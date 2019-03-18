package io.github.lr.querythreads.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.lr.querythreads.entities.Baby;

/**
 * Repositorio con acceso paginado.
 * 
 * @author lravanal
 *
 */
@Repository
public interface BabyRepository extends JpaRepository<Baby, Long> {

}
