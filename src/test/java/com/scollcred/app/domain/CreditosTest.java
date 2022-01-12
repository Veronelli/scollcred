package com.scollcred.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.scollcred.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CreditosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Creditos.class);
        Creditos creditos1 = new Creditos();
        creditos1.setId(1L);
        Creditos creditos2 = new Creditos();
        creditos2.setId(creditos1.getId());
        assertThat(creditos1).isEqualTo(creditos2);
        creditos2.setId(2L);
        assertThat(creditos1).isNotEqualTo(creditos2);
        creditos1.setId(null);
        assertThat(creditos1).isNotEqualTo(creditos2);
    }
}
