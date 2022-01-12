package com.scollcred.app.web.rest;

import com.scollcred.app.domain.Dependencia;
import com.scollcred.app.repository.DependenciaRepository;
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
 * REST controller for managing {@link com.scollcred.app.domain.Dependencia}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DependenciaResource {

    private final Logger log = LoggerFactory.getLogger(DependenciaResource.class);

    private static final String ENTITY_NAME = "dependencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DependenciaRepository dependenciaRepository;

    public DependenciaResource(DependenciaRepository dependenciaRepository) {
        this.dependenciaRepository = dependenciaRepository;
    }

    /**
     * {@code POST  /dependencias} : Create a new dependencia.
     *
     * @param dependencia the dependencia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dependencia, or with status {@code 400 (Bad Request)} if the dependencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dependencias")
    public ResponseEntity<Dependencia> createDependencia(@Valid @RequestBody Dependencia dependencia) throws URISyntaxException {
        log.debug("REST request to save Dependencia : {}", dependencia);
        if (dependencia.getId() != null) {
            throw new BadRequestAlertException("A new dependencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dependencia result = dependenciaRepository.save(dependencia);
        return ResponseEntity
            .created(new URI("/api/dependencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dependencias/:id} : Updates an existing dependencia.
     *
     * @param id the id of the dependencia to save.
     * @param dependencia the dependencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependencia,
     * or with status {@code 400 (Bad Request)} if the dependencia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dependencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dependencias/{id}")
    public ResponseEntity<Dependencia> updateDependencia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Dependencia dependencia
    ) throws URISyntaxException {
        log.debug("REST request to update Dependencia : {}, {}", id, dependencia);
        if (dependencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dependencia result = dependenciaRepository.save(dependencia);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dependencia.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dependencias/:id} : Partial updates given fields of an existing dependencia, field will ignore if it is null
     *
     * @param id the id of the dependencia to save.
     * @param dependencia the dependencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependencia,
     * or with status {@code 400 (Bad Request)} if the dependencia is not valid,
     * or with status {@code 404 (Not Found)} if the dependencia is not found,
     * or with status {@code 500 (Internal Server Error)} if the dependencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dependencias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dependencia> partialUpdateDependencia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Dependencia dependencia
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dependencia partially : {}, {}", id, dependencia);
        if (dependencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dependencia> result = dependenciaRepository
            .findById(dependencia.getId())
            .map(existingDependencia -> {
                if (dependencia.getEmpleador() != null) {
                    existingDependencia.setEmpleador(dependencia.getEmpleador());
                }

                return existingDependencia;
            })
            .map(dependenciaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dependencia.getId().toString())
        );
    }

    /**
     * {@code GET  /dependencias} : get all the dependencias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dependencias in body.
     */
    @GetMapping("/dependencias")
    public List<Dependencia> getAllDependencias() {
        log.debug("REST request to get all Dependencias");
        return dependenciaRepository.findAll();
    }

    /**
     * {@code GET  /dependencias/:id} : get the "id" dependencia.
     *
     * @param id the id of the dependencia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dependencia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dependencias/{id}")
    public ResponseEntity<Dependencia> getDependencia(@PathVariable Long id) {
        log.debug("REST request to get Dependencia : {}", id);
        Optional<Dependencia> dependencia = dependenciaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dependencia);
    }

    /**
     * {@code DELETE  /dependencias/:id} : delete the "id" dependencia.
     *
     * @param id the id of the dependencia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dependencias/{id}")
    public ResponseEntity<Void> deleteDependencia(@PathVariable Long id) {
        log.debug("REST request to delete Dependencia : {}", id);
        dependenciaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
