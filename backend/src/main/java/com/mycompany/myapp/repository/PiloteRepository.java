package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pilote;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pilote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PiloteRepository extends JpaRepository<Pilote, Long> {

}
