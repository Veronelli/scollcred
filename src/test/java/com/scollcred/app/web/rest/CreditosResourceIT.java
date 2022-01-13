package com.scollcred.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.scollcred.app.IntegrationTest;
import com.scollcred.app.domain.Creditos;
import com.scollcred.app.domain.Mutual;
import com.scollcred.app.repository.CreditosRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CreditosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CreditosResourceIT {

    private static final Integer DEFAULT_MONTO = 1;
    private static final Integer UPDATED_MONTO = 2;

    private static final Integer DEFAULT_PAGO_CUOTA = 1;
    private static final Integer UPDATED_PAGO_CUOTA = 2;

    private static final Integer DEFAULT_CANTIDAD_CUOTAS = 1;
    private static final Integer UPDATED_CANTIDAD_CUOTAS = 2;

    private static final LocalDate DEFAULT_TOMADO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TOMADO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_INICIO_PAGO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INICIO_PAGO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/creditos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CreditosRepository creditosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreditosMockMvc;

    private Creditos creditos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Creditos createEntity(EntityManager em) {
        Creditos creditos = new Creditos()
            .monto(DEFAULT_MONTO)
            .pagoCuota(DEFAULT_PAGO_CUOTA)
            .cantidadCuotas(DEFAULT_CANTIDAD_CUOTAS)
            .tomado(DEFAULT_TOMADO)
            .inicioPago(DEFAULT_INICIO_PAGO);
        // Add required entity
        Mutual mutual;
        if (TestUtil.findAll(em, Mutual.class).isEmpty()) {
            mutual = MutualResourceIT.createEntity(em);
            em.persist(mutual);
            em.flush();
        } else {
            mutual = TestUtil.findAll(em, Mutual.class).get(0);
        }
        creditos.setMutual(mutual);
        return creditos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Creditos createUpdatedEntity(EntityManager em) {
        Creditos creditos = new Creditos()
            .monto(UPDATED_MONTO)
            .pagoCuota(UPDATED_PAGO_CUOTA)
            .cantidadCuotas(UPDATED_CANTIDAD_CUOTAS)
            .tomado(UPDATED_TOMADO)
            .inicioPago(UPDATED_INICIO_PAGO);
        // Add required entity
        Mutual mutual;
        if (TestUtil.findAll(em, Mutual.class).isEmpty()) {
            mutual = MutualResourceIT.createUpdatedEntity(em);
            em.persist(mutual);
            em.flush();
        } else {
            mutual = TestUtil.findAll(em, Mutual.class).get(0);
        }
        creditos.setMutual(mutual);
        return creditos;
    }

    @BeforeEach
    public void initTest() {
        creditos = createEntity(em);
    }

    @Test
    @Transactional
    void createCreditos() throws Exception {
        int databaseSizeBeforeCreate = creditosRepository.findAll().size();
        // Create the Creditos
        restCreditosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isCreated());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeCreate + 1);
        Creditos testCreditos = creditosList.get(creditosList.size() - 1);
        assertThat(testCreditos.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testCreditos.getPagoCuota()).isEqualTo(DEFAULT_PAGO_CUOTA);
        assertThat(testCreditos.getCantidadCuotas()).isEqualTo(DEFAULT_CANTIDAD_CUOTAS);
        assertThat(testCreditos.getTomado()).isEqualTo(DEFAULT_TOMADO);
        assertThat(testCreditos.getInicioPago()).isEqualTo(DEFAULT_INICIO_PAGO);
    }

    @Test
    @Transactional
    void createCreditosWithExistingId() throws Exception {
        // Create the Creditos with an existing ID
        creditos.setId(1L);

        int databaseSizeBeforeCreate = creditosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isBadRequest());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditosRepository.findAll().size();
        // set the field null
        creditos.setMonto(null);

        // Create the Creditos, which fails.

        restCreditosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isBadRequest());

        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPagoCuotaIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditosRepository.findAll().size();
        // set the field null
        creditos.setPagoCuota(null);

        // Create the Creditos, which fails.

        restCreditosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isBadRequest());

        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadCuotasIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditosRepository.findAll().size();
        // set the field null
        creditos.setCantidadCuotas(null);

        // Create the Creditos, which fails.

        restCreditosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isBadRequest());

        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTomadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditosRepository.findAll().size();
        // set the field null
        creditos.setTomado(null);

        // Create the Creditos, which fails.

        restCreditosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isBadRequest());

        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInicioPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditosRepository.findAll().size();
        // set the field null
        creditos.setInicioPago(null);

        // Create the Creditos, which fails.

        restCreditosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isBadRequest());

        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCreditos() throws Exception {
        // Initialize the database
        creditosRepository.saveAndFlush(creditos);

        // Get all the creditosList
        restCreditosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditos.getId().intValue())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO)))
            .andExpect(jsonPath("$.[*].pagoCuota").value(hasItem(DEFAULT_PAGO_CUOTA)))
            .andExpect(jsonPath("$.[*].cantidadCuotas").value(hasItem(DEFAULT_CANTIDAD_CUOTAS)))
            .andExpect(jsonPath("$.[*].tomado").value(hasItem(DEFAULT_TOMADO.toString())))
            .andExpect(jsonPath("$.[*].inicioPago").value(hasItem(DEFAULT_INICIO_PAGO.toString())));
    }

    @Test
    @Transactional
    void getCreditos() throws Exception {
        // Initialize the database
        creditosRepository.saveAndFlush(creditos);

        // Get the creditos
        restCreditosMockMvc
            .perform(get(ENTITY_API_URL_ID, creditos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creditos.getId().intValue()))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO))
            .andExpect(jsonPath("$.pagoCuota").value(DEFAULT_PAGO_CUOTA))
            .andExpect(jsonPath("$.cantidadCuotas").value(DEFAULT_CANTIDAD_CUOTAS))
            .andExpect(jsonPath("$.tomado").value(DEFAULT_TOMADO.toString()))
            .andExpect(jsonPath("$.inicioPago").value(DEFAULT_INICIO_PAGO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCreditos() throws Exception {
        // Get the creditos
        restCreditosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCreditos() throws Exception {
        // Initialize the database
        creditosRepository.saveAndFlush(creditos);

        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();

        // Update the creditos
        Creditos updatedCreditos = creditosRepository.findById(creditos.getId()).get();
        // Disconnect from session so that the updates on updatedCreditos are not directly saved in db
        em.detach(updatedCreditos);
        updatedCreditos
            .monto(UPDATED_MONTO)
            .pagoCuota(UPDATED_PAGO_CUOTA)
            .cantidadCuotas(UPDATED_CANTIDAD_CUOTAS)
            .tomado(UPDATED_TOMADO)
            .inicioPago(UPDATED_INICIO_PAGO);

        restCreditosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCreditos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCreditos))
            )
            .andExpect(status().isOk());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
        Creditos testCreditos = creditosList.get(creditosList.size() - 1);
        assertThat(testCreditos.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testCreditos.getPagoCuota()).isEqualTo(UPDATED_PAGO_CUOTA);
        assertThat(testCreditos.getCantidadCuotas()).isEqualTo(UPDATED_CANTIDAD_CUOTAS);
        assertThat(testCreditos.getTomado()).isEqualTo(UPDATED_TOMADO);
        assertThat(testCreditos.getInicioPago()).isEqualTo(UPDATED_INICIO_PAGO);
    }

    @Test
    @Transactional
    void putNonExistingCreditos() throws Exception {
        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();
        creditos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreditos() throws Exception {
        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();
        creditos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreditos() throws Exception {
        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();
        creditos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCreditosWithPatch() throws Exception {
        // Initialize the database
        creditosRepository.saveAndFlush(creditos);

        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();

        // Update the creditos using partial update
        Creditos partialUpdatedCreditos = new Creditos();
        partialUpdatedCreditos.setId(creditos.getId());

        partialUpdatedCreditos.monto(UPDATED_MONTO).pagoCuota(UPDATED_PAGO_CUOTA).tomado(UPDATED_TOMADO);

        restCreditosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditos))
            )
            .andExpect(status().isOk());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
        Creditos testCreditos = creditosList.get(creditosList.size() - 1);
        assertThat(testCreditos.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testCreditos.getPagoCuota()).isEqualTo(UPDATED_PAGO_CUOTA);
        assertThat(testCreditos.getCantidadCuotas()).isEqualTo(DEFAULT_CANTIDAD_CUOTAS);
        assertThat(testCreditos.getTomado()).isEqualTo(UPDATED_TOMADO);
        assertThat(testCreditos.getInicioPago()).isEqualTo(DEFAULT_INICIO_PAGO);
    }

    @Test
    @Transactional
    void fullUpdateCreditosWithPatch() throws Exception {
        // Initialize the database
        creditosRepository.saveAndFlush(creditos);

        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();

        // Update the creditos using partial update
        Creditos partialUpdatedCreditos = new Creditos();
        partialUpdatedCreditos.setId(creditos.getId());

        partialUpdatedCreditos
            .monto(UPDATED_MONTO)
            .pagoCuota(UPDATED_PAGO_CUOTA)
            .cantidadCuotas(UPDATED_CANTIDAD_CUOTAS)
            .tomado(UPDATED_TOMADO)
            .inicioPago(UPDATED_INICIO_PAGO);

        restCreditosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditos))
            )
            .andExpect(status().isOk());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
        Creditos testCreditos = creditosList.get(creditosList.size() - 1);
        assertThat(testCreditos.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testCreditos.getPagoCuota()).isEqualTo(UPDATED_PAGO_CUOTA);
        assertThat(testCreditos.getCantidadCuotas()).isEqualTo(UPDATED_CANTIDAD_CUOTAS);
        assertThat(testCreditos.getTomado()).isEqualTo(UPDATED_TOMADO);
        assertThat(testCreditos.getInicioPago()).isEqualTo(UPDATED_INICIO_PAGO);
    }

    @Test
    @Transactional
    void patchNonExistingCreditos() throws Exception {
        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();
        creditos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, creditos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreditos() throws Exception {
        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();
        creditos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreditos() throws Exception {
        int databaseSizeBeforeUpdate = creditosRepository.findAll().size();
        creditos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(creditos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Creditos in the database
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCreditos() throws Exception {
        // Initialize the database
        creditosRepository.saveAndFlush(creditos);

        int databaseSizeBeforeDelete = creditosRepository.findAll().size();

        // Delete the creditos
        restCreditosMockMvc
            .perform(delete(ENTITY_API_URL_ID, creditos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Creditos> creditosList = creditosRepository.findAll();
        assertThat(creditosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
