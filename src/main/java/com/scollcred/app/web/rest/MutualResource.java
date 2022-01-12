package com.scollcred.app.web.rest;

import com.scollcred.app.domain.Mutual;
import com.scollcred.app.repository.MutualRepository;
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
 * REST controller for managing {@link com.scollcred.app.domain.Mutual}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MutualResource {

    private final Logger log = LoggerFactory.getLogger(MutualResource.class);

    private static final String ENTITY_NAME = "mutual";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MutualRepository mutualRepository;

    public MutualResource(MutualRepository mutualRepository) {
        this.mutualRepository = mutualRepository;
    }

    /**
     * {@code POST  /mutuals} : Create a new mutual.
     *
     * @param mutual the mutual to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mutual, or with status {@code 400 (Bad Request)} if the mutual has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mutuals")
    public ResponseEntity<Mutual> createMutual(@Valid @RequestBody Mutual mutual) throws URISyntaxException {
        log.debug("REST request to save Mutual : {}", mutual);
        if (mutual.getId() != null) {
            throw new BadRequestAlertException("A new mutual cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mutual result = mutualRepository.save(mutual);
        return ResponseEntity
            .created(new URI("/api/mutuals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mutuals/:id} : Updates an existing mutual.
     *
     * @param id the id of the mutual to save.
     * @param mutual the mutual to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mutual,
     * or with status {@code 400 (Bad Request)} if the mutual is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mutual couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mutuals/{id}")
    public ResponseEntity<Mutual> updateMutual(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mutual mutual
    ) throws URISyntaxException {
        log.debug("REST request to update Mutual : {}, {}", id, mutual);
        if (mutual.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mutual.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mutualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mutual result = mutualRepository.save(mutual);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mutual.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mutuals/:id} : Partial updates given fields of an existing mutual, field will ignore if it is null
     *
     * @param id the id of the mutual to save.
     * @param mutual the mutual to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mutual,
     * or with status {@code 400 (Bad Request)} if the mutual is not valid,
     * or with status {@code 404 (Not Found)} if the mutual is not found,
     * or with status {@code 500 (Internal Server Error)} if the mutual couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mutuals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mutual> partialUpdateMutual(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mutual mutual
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mutual partially : {}, {}", id, mutual);
        if (mutual.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mutual.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mutualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mutual> result = mutualRepository
            .findById(mutual.getId())
            .map(existingMutual -> {
                if (mutual.getNombre() != null) {
                    existingMutual.setNombre(mutual.getNombre());
                }

                return existingMutual;
            })
            .map(mutualRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mutual.getId().toString())
        );
    }

    /**
     * {@code GET  /mutuals} : get all the mutuals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mutuals in body.
     */
    @GetMapping("/mutuals")
    public List<Mutual> getAllMutuals() {
        log.debug("REST request to get all Mutuals");
        return mutualRepository.findAll();
    }

    /**
     * {@code GET  /mutuals/:id} : get the "id" mutual.
     *
     * @param id the id of the mutual to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mutual, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mutuals/{id}")
    public ResponseEntity<Mutual> getMutual(@PathVariable Long id) {
        log.debug("REST request to get Mutual : {}", id);
        Optional<Mutual> mutual = mutualRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mutual);
    }

    /**
     * {@code DELETE  /mutuals/:id} : delete the "id" mutual.
     *
     * @param id the id of the mutual to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mutuals/{id}")
    public ResponseEntity<Void> deleteMutual(@PathVariable Long id) {
        log.debug("REST request to delete Mutual : {}", id);
        mutualRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
