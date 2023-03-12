package com.dhl.assetmanager.repository;

import com.dhl.assetmanager.entity.GlobalVariable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GlobalVariableRepository extends Repository<GlobalVariable, String> {

    Optional<GlobalVariable> findByKey(String key);

    void save(GlobalVariable user);

}
