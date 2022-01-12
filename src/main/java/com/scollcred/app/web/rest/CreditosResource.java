package com.scollcred.app.web.rest;

import com.scollcred.app.domain.Creditos;
import com.scollcred.app.repository.CreditosRepository;
import com.scollcred.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.scollcred.app.domain.Creditos}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CreditosResource {

    private final Logger log = LoggerFactory.getLogger(CreditosResource.class);

    private static final String ENTITY_NAME = "creditos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreditosRepository creditosRepository;

    public CreditosResource(CreditosRepository creditosRepository) {
        this.creditosRepository = creditosRepository;
    }

    /**
     * {@code POST  /creditos} : Create a new creditos.
     *
     * @param creditos the creditos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new creditos, or with status {@code 400 (Bad Request)} if the creditos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/creditos")
    public ResponseEntity<Creditos> createCreditos(@Valid @RequestBody Creditos creditos) throws URISyntaxException {
        log.debug("REST request to save Creditos : {}", creditos);
        if (creditos.getId() != null) {
            throw new BadRequestAlertException("A new creditos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Creditos result = creditosRepository.save(creditos);
        return ResponseEntity
            .created(new URI("/api/creditos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /creditos/:id} : Updates an existing creditos.
     *
     * @param id the id of the creditos to save.
     * @param creditos the creditos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditos,
     * or with status {@code 400 (Bad Request)} if the creditos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creditos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/creditos/{id}")
    public ResponseEntity<Creditos> updateCreditos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Creditos creditos
    ) throws URISyntaxException {
        log.debug("REST request to update Creditos : {}, {}", id, creditos);
        if (creditos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Creditos result = creditosRepository.save(creditos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, creditos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /creditos/:id} : Partial updates given fields of an existing creditos, field will ignore if it is null
     *
     * @param id the id of the creditos to save.
     * @param creditos the creditos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditos,
     * or with status {@code 400 (Bad Request)} if the creditos is not valid,
     * or with status {@code 404 (Not Found)} if the creditos is not found,
     * or with status {@code 500 (Internal Server Error)} if the creditos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/creditos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Creditos> partialUpdateCreditos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Creditos creditos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Creditos partially : {}, {}", id, creditos);
        if (creditos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Creditos> result = creditosRepository
            .findById(creditos.getId())
            .map(existingCreditos -> {
                if (creditos.getEmisionCuotas() != null) {
                    existingCreditos.setEmisionCuotas(creditos.getEmisionCuotas());
                }
                if (creditos.getMonto() != null) {
                    existingCreditos.setMonto(creditos.getMonto());
                }
                if (creditos.getPagoCuota() != null) {
                    existingCreditos.setPagoCuota(creditos.getPagoCuota());
                }
                if (creditos.getCantidadCuotas() != null) {
                    existingCreditos.setCantidadCuotas(creditos.getCantidadCuotas());
                }
                if (creditos.getTomado() != null) {
                    existingCreditos.setTomado(creditos.getTomado());
                }
                if (creditos.getInicioPago() != null) {
                    existingCreditos.setInicioPago(creditos.getInicioPago());
                }

                return existingCreditos;
            })
            .map(creditosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, creditos.getId().toString())
        );
    }

    /**
     * {@code GET  /creditos} : get all the creditos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of creditos in body.
     */
    @GetMapping("/creditos")
    public List<Creditos> getAllCreditos() {
        log.debug("REST request to get all Creditos");
        return creditosRepository.findAll();
    }

    /**
     * {@code GET  /creditos/:id} : get the "id" creditos.
     *
     * @param id the id of the creditos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the creditos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/creditos/{id}")
    public ResponseEntity<Creditos> getCreditos(@PathVariable Long id) {
        log.debug("REST request to get Creditos : {}", id);
        Optional<Creditos> creditos = creditosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(creditos);
    }

    /**
     * {@code DELETE  /creditos/:id} : delete the "id" creditos.
     *
     * @param id the id of the creditos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/creditos/{id}")
    public ResponseEntity<Void> deleteCreditos(@PathVariable Long id) {
        log.debug("REST request to delete Creditos : {}", id);
        creditosRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
