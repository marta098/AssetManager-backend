package com.dhl.assetmanager.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TemplateServiceTest {

    @Autowired
    private TemplateService templateService;

    @Test
    void shouldReturnTemplateWithGivenData() {
        var givenTemplate = "test-template";
        Map<String, Object> givenData = Map.of("test1", "test-value", "test2", "another test value");

        var actualText = templateService.evaluate(givenTemplate, givenData);

        assertThat(actualText)
                .isNotBlank()
                .contains("<span>test-value</span>")
                .contains("<span>another test value</span>");
    }

}
