package com.hrms.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormValidatorMapperTest {

    private FormValidatorMapper formValidatorMapper;

    @BeforeEach
    public void setUp() {
        formValidatorMapper = new FormValidatorMapperImpl();
    }
}
