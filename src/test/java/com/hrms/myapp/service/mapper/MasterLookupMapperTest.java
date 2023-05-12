package com.hrms.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MasterLookupMapperTest {

    private MasterLookupMapper masterLookupMapper;

    @BeforeEach
    public void setUp() {
        masterLookupMapper = new MasterLookupMapperImpl();
    }
}
