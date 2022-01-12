package com.scollcred.app.repository;

import com.scollcred.app.domain.Mutual;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mutual entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MutualRepository extends JpaRepository<Mutual, Long> {}
