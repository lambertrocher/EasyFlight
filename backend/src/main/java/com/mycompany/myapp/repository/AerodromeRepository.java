package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Aerodrome;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Aerodrome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AerodromeRepository extends JpaRepository<Aerodrome, Long> {

}
