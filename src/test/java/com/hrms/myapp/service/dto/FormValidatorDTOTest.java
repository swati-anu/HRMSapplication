package com.hrms.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrms.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormValidatorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormValidatorDTO.class);
        FormValidatorDTO formValidatorDTO1 = new FormValidatorDTO();
        formValidatorDTO1.setId(1L);
        FormValidatorDTO formValidatorDTO2 = new FormValidatorDTO();
        assertThat(formValidatorDTO1).isNotEqualTo(formValidatorDTO2);
        formValidatorDTO2.setId(formValidatorDTO1.getId());
        assertThat(formValidatorDTO1).isEqualTo(formValidatorDTO2);
        formValidatorDTO2.setId(2L);
        assertThat(formValidatorDTO1).isNotEqualTo(formValidatorDTO2);
        formValidatorDTO1.setId(null);
        assertThat(formValidatorDTO1).isNotEqualTo(formValidatorDTO2);
    }
}
