package com.scollcred.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.scollcred.app.IntegrationTest;
import com.scollcred.app.domain.Mutual;
import com.scollcred.app.repository.MutualRepository;
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
 * Integration tests for the {@link MutualResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MutualResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mutuals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MutualRepository mutualRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMutualMockMvc;

    private Mutual mutual;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mutual createEntity(EntityManager em) {
        Mutual mutual = new Mutual().nombre(DEFAULT_NOMBRE);
        return mutual;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mutual createUpdatedEntity(EntityManager em) {
        Mutual mutual = new Mutual().nombre(UPDATED_NOMBRE);
        return mutual;
    }

    @BeforeEach
    public void initTest() {
        mutual = createEntity(em);
    }

    @Test
    @Transactional
    void createMutual() throws Exception {
        int databaseSizeBeforeCreate = mutualRepository.findAll().size();
        // Create the Mutual
        restMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mutual)))
            .andExpect(status().isCreated());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeCreate + 1);
        Mutual testMutual = mutualList.get(mutualList.size() - 1);
        assertThat(testMutual.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createMutualWithExistingId() throws Exception {
        // Create the Mutual with an existing ID
        mutual.setId(1L);

        int databaseSizeBeforeCreate = mutualRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mutual)))
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = mutualRepository.findAll().size();
        // set the field null
        mutual.setNombre(null);

        // Create the Mutual, which fails.

        restMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mutual)))
            .andExpect(status().isBadRequest());

        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMutuals() throws Exception {
        // Initialize the database
        mutualRepository.saveAndFlush(mutual);

        // Get all the mutualList
        restMutualMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mutual.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getMutual() throws Exception {
        // Initialize the database
        mutualRepository.saveAndFlush(mutual);

        // Get the mutual
        restMutualMockMvc
            .perform(get(ENTITY_API_URL_ID, mutual.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mutual.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingMutual() throws Exception {
        // Get the mutual
        restMutualMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMutual() throws Exception {
        // Initialize the database
        mutualRepository.saveAndFlush(mutual);

        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();

        // Update the mutual
        Mutual updatedMutual = mutualRepository.findById(mutual.getId()).get();
        // Disconnect from session so that the updates on updatedMutual are not directly saved in db
        em.detach(updatedMutual);
        updatedMutual.nombre(UPDATED_NOMBRE);

        restMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMutual.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMutual))
            )
            .andExpect(status().isOk());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
        Mutual testMutual = mutualList.get(mutualList.size() - 1);
        assertThat(testMutual.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingMutual() throws Exception {
        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();
        mutual.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mutual.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mutual))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMutual() throws Exception {
        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();
        mutual.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mutual))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMutual() throws Exception {
        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();
        mutual.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mutual)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMutualWithPatch() throws Exception {
        // Initialize the database
        mutualRepository.saveAndFlush(mutual);

        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();

        // Update the mutual using partial update
        Mutual partialUpdatedMutual = new Mutual();
        partialUpdatedMutual.setId(mutual.getId());

        restMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMutual.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMutual))
            )
            .andExpect(status().isOk());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
        Mutual testMutual = mutualList.get(mutualList.size() - 1);
        assertThat(testMutual.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateMutualWithPatch() throws Exception {
        // Initialize the database
        mutualRepository.saveAndFlush(mutual);

        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();

        // Update the mutual using partial update
        Mutual partialUpdatedMutual = new Mutual();
        partialUpdatedMutual.setId(mutual.getId());

        partialUpdatedMutual.nombre(UPDATED_NOMBRE);

        restMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMutual.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMutual))
            )
            .andExpect(status().isOk());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
        Mutual testMutual = mutualList.get(mutualList.size() - 1);
        assertThat(testMutual.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingMutual() throws Exception {
        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();
        mutual.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mutual.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mutual))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMutual() throws Exception {
        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();
        mutual.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mutual))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMutual() throws Exception {
        int databaseSizeBeforeUpdate = mutualRepository.findAll().size();
        mutual.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mutual)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mutual in the database
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMutual() throws Exception {
        // Initialize the database
        mutualRepository.saveAndFlush(mutual);

        int databaseSizeBeforeDelete = mutualRepository.findAll().size();

        // Delete the mutual
        restMutualMockMvc
            .perform(delete(ENTITY_API_URL_ID, mutual.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mutual> mutualList = mutualRepository.findAll();
        assertThat(mutualList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
