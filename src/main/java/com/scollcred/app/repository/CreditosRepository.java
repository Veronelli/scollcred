package com.scollcred.app.repository;

import com.scollcred.app.domain.Creditos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Creditos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreditosRepository extends JpaRepository<Creditos, Long> {}
