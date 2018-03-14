package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Piste;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Piste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PisteRepository extends JpaRepository<Piste, Long> {

}
