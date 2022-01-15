package com.scollcred.app.repository;

import com.scollcred.app.domain.Cliente;
import com.scollcred.app.domain.Creditos;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Creditos entity.
 */
@SuppressWarnings("unused")
@EnableJpaRepositories
@Repository
public interface CreditosRepository extends JpaRepository<Creditos, Long> {
    @Query(value="SELECT cred.* FROM CREDITOS AS cred LEFT JOIN CLIENTE AS clie  ON clie.ID = cred.ID LEFT JOIN MUTUAL AS mut ON clie.ID = mut.id WHERE clie.NOMBRE LIKE :cliente% AND mut.NOMBRE LIKE :mutual%", nativeQuery = true)
    public List<Creditos> findByClienteAndMutual(@Param("cliente") String cliente,@Param("mutual") String mutual);

    @Query(value="SELECT cred.* FROM CREDITOS AS cred LEFT JOIN CLIENTE AS clie ON clie.ID = cred.ID WHERE clie.NOMBRE LIKE :cliente%", nativeQuery = true)
    public List<Creditos> findByClienteName(@Param("cliente")String cliente);

    @Query(value="SELECT cred.* FROM CREDITOS AS cred LEFT JOIN MUTUAL AS mut ON mut.ID = cred.ID WHERE mut.NOMBRE LIKE :mutual%", nativeQuery = true)
    public List<Creditos> findByMutualName(@Param("mutual")String mutual);

}
