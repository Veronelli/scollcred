package com.scollcred.app.repository;

import com.scollcred.app.domain.Dependencia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dependencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DependenciaRepository extends JpaRepository<Dependencia, Long> {}
