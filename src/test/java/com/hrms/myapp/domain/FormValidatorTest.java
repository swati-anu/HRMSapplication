package com.hrms.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrms.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormValidatorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormValidator.class);
        FormValidator formValidator1 = new FormValidator();
        formValidator1.setId(1L);
        FormValidator formValidator2 = new FormValidator();
        formValidator2.setId(formValidator1.getId());
        assertThat(formValidator1).isEqualTo(formValidator2);
        formValidator2.setId(2L);
        assertThat(formValidator1).isNotEqualTo(formValidator2);
        formValidator1.setId(null);
        assertThat(formValidator1).isNotEqualTo(formValidator2);
    }
}
