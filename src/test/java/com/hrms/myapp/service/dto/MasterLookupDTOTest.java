package com.hrms.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrms.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterLookupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterLookupDTO.class);
        MasterLookupDTO masterLookupDTO1 = new MasterLookupDTO();
        masterLookupDTO1.setId(1L);
        MasterLookupDTO masterLookupDTO2 = new MasterLookupDTO();
        assertThat(masterLookupDTO1).isNotEqualTo(masterLookupDTO2);
        masterLookupDTO2.setId(masterLookupDTO1.getId());
        assertThat(masterLookupDTO1).isEqualTo(masterLookupDTO2);
        masterLookupDTO2.setId(2L);
        assertThat(masterLookupDTO1).isNotEqualTo(masterLookupDTO2);
        masterLookupDTO1.setId(null);
        assertThat(masterLookupDTO1).isNotEqualTo(masterLookupDTO2);
    }
}
