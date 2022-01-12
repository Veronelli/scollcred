package com.scollcred.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.scollcred.app.IntegrationTest;
import com.scollcred.app.domain.Dependencia;
import com.scollcred.app.repository.DependenciaRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DependenciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DependenciaResourceIT {

    private static final String DEFAULT_EMPLEADOR = "AAAAAAAAAA";
    private static final String UPDATED_EMPLEADOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dependencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDependenciaMockMvc;

    private Dependencia dependencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependencia createEntity(EntityManager em) {
        Dependencia dependencia = new Dependencia().empleador(DEFAULT_EMPLEADOR);
        return dependencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependencia createUpdatedEntity(EntityManager em) {
        Dependencia dependencia = new Dependencia().empleador(UPDATED_EMPLEADOR);
        return dependencia;
    }

    @BeforeEach
    public void initTest() {
        dependencia = createEntity(em);
    }

    @Test
    @Transactional
    void createDependencia() throws Exception {
        int databaseSizeBeforeCreate = dependenciaRepository.findAll().size();
        // Create the Dependencia
        restDependenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependencia)))
            .andExpect(status().isCreated());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getEmpleador()).isEqualTo(DEFAULT_EMPLEADOR);
    }

    @Test
    @Transactional
    void createDependenciaWithExistingId() throws Exception {
        // Create the Dependencia with an existing ID
        dependencia.setId(1L);

        int databaseSizeBeforeCreate = dependenciaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDependenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependencia)))
            .andExpect(status().isBadRequest());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmpleadorIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependenciaRepository.findAll().size();
        // set the field null
        dependencia.setEmpleador(null);

        // Create the Dependencia, which fails.

        restDependenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependencia)))
            .andExpect(status().isBadRequest());

        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDependencias() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        // Get all the dependenciaList
        restDependenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].empleador").value(hasItem(DEFAULT_EMPLEADOR)));
    }

    @Test
    @Transactional
    void getDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        // Get the dependencia
        restDependenciaMockMvc
            .perform(get(ENTITY_API_URL_ID, dependencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dependencia.getId().intValue()))
            .andExpect(jsonPath("$.empleador").value(DEFAULT_EMPLEADOR));
    }

    @Test
    @Transactional
    void getNonExistingDependencia() throws Exception {
        // Get the dependencia
        restDependenciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();

        // Update the dependencia
        Dependencia updatedDependencia = dependenciaRepository.findById(dependencia.getId()).get();
        // Disconnect from session so that the updates on updatedDependencia are not directly saved in db
        em.detach(updatedDependencia);
        updatedDependencia.empleador(UPDATED_EMPLEADOR);

        restDependenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDependencia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDependencia))
            )
            .andExpect(status().isOk());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getEmpleador()).isEqualTo(UPDATED_EMPLEADOR);
    }

    @Test
    @Transactional
    void putNonExistingDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();
        dependencia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependencia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();
        dependencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();
        dependencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependencia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDependenciaWithPatch() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();

        // Update the dependencia using partial update
        Dependencia partialUpdatedDependencia = new Dependencia();
        partialUpdatedDependencia.setId(dependencia.getId());

        restDependenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDependencia))
            )
            .andExpect(status().isOk());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getEmpleador()).isEqualTo(DEFAULT_EMPLEADOR);
    }

    @Test
    @Transactional
    void fullUpdateDependenciaWithPatch() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();

        // Update the dependencia using partial update
        Dependencia partialUpdatedDependencia = new Dependencia();
        partialUpdatedDependencia.setId(dependencia.getId());

        partialUpdatedDependencia.empleador(UPDATED_EMPLEADOR);

        restDependenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDependencia))
            )
            .andExpect(status().isOk());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
        Dependencia testDependencia = dependenciaList.get(dependenciaList.size() - 1);
        assertThat(testDependencia.getEmpleador()).isEqualTo(UPDATED_EMPLEADOR);
    }

    @Test
    @Transactional
    void patchNonExistingDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();
        dependencia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dependencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dependencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();
        dependencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dependencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDependencia() throws Exception {
        int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();
        dependencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependenciaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dependencia))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dependencia in the database
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        int databaseSizeBeforeDelete = dependenciaRepository.findAll().size();

        // Delete the dependencia
        restDependenciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, dependencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dependencia> dependenciaList = dependenciaRepository.findAll();
        assertThat(dependenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
