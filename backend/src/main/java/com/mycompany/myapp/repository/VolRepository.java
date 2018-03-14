package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Vol;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Vol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VolRepository extends JpaRepository<Vol, Long> {

}
