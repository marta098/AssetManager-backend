package com.dhl.assetmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateEngine templateEngine;

    public String evaluate(String templateName, Map<String, Object> data) {
        var context = new Context();
        context.setVariables(data);
        return templateEngine.process(templateName, context);
    }

}
