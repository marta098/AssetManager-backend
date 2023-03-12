package com.dhl.assetmanager.service;

import com.dhl.assetmanager.exception.GlobalVariableNotFoundException;
import com.dhl.assetmanager.repository.GlobalVariableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GlobalVariableService {

    private static final String DEPRECATION_MONTHS = "deprecation_months";
    private static final String CREST_CODE = "crest_code";

    private final GlobalVariableRepository globalVariableRepository;

    public int getDeprecationMonths() {
        var globalVariable = globalVariableRepository.findByKey(DEPRECATION_MONTHS)
                .orElseThrow(GlobalVariableNotFoundException::new);
        return Integer.parseInt(globalVariable.getValue());
    }

    @Transactional
    public void setDeprecationMonths(int deprecationMonths) {
        var globalVariable = globalVariableRepository.findByKey(DEPRECATION_MONTHS)
                .orElseThrow(GlobalVariableNotFoundException::new);
        globalVariable.setValue(String.valueOf(deprecationMonths));
        globalVariableRepository.save(globalVariable);
    }

    public String getCrestCode() {
        var globalVariable = globalVariableRepository.findByKey(CREST_CODE)
                .orElseThrow(GlobalVariableNotFoundException::new);
        return globalVariable.getValue();
    }

    @Transactional
    public void setCrestCode(String crestCode) {
        var globalVariable = globalVariableRepository.findByKey(CREST_CODE)
                .orElseThrow(GlobalVariableNotFoundException::new);
        globalVariable.setValue(crestCode);
        globalVariableRepository.save(globalVariable);
    }
}
