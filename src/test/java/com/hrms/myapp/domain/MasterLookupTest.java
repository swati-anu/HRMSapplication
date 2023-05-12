package com.hrms.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrms.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterLookupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterLookup.class);
        MasterLookup masterLookup1 = new MasterLookup();
        masterLookup1.setId(1L);
        MasterLookup masterLookup2 = new MasterLookup();
        masterLookup2.setId(masterLookup1.getId());
        assertThat(masterLookup1).isEqualTo(masterLookup2);
        masterLookup2.setId(2L);
        assertThat(masterLookup1).isNotEqualTo(masterLookup2);
        masterLookup1.setId(null);
        assertThat(masterLookup1).isNotEqualTo(masterLookup2);
    }
}
