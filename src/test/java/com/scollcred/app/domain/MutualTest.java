package com.scollcred.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.scollcred.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MutualTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mutual.class);
        Mutual mutual1 = new Mutual();
        mutual1.setId(1L);
        Mutual mutual2 = new Mutual();
        mutual2.setId(mutual1.getId());
        assertThat(mutual1).isEqualTo(mutual2);
        mutual2.setId(2L);
        assertThat(mutual1).isNotEqualTo(mutual2);
        mutual1.setId(null);
        assertThat(mutual1).isNotEqualTo(mutual2);
    }
}
